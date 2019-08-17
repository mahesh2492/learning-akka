package effective.service

import akka.actor.{Actor, ActorLogging, ActorRef, Cancellable, Props}
import akka.event.LoggingReceive
import effective._

import scala.concurrent.duration._

object AccountBalanceRetrieverFinal {

  case object AccountRetrievalTimeout

}

class AccountBalanceRetriever(savingsAccounts: ActorRef,
                              checkingAccounts: ActorRef,
                              moneyMarketAccounts: ActorRef) extends Actor with ActorLogging {

  import AccountBalanceRetrieverFinal._

  var checkingBalances, savingsBalances, mmBalances: Option[List[(Long, BigDecimal)]] = None

  def receive: PartialFunction[Any, Unit] = {
    case GetCustomerAccountBalances(id) => {
      log.debug(s"Received GetCustomerAccountBalances for Id: $id from $sender")
      val originalSender = sender()
      context.actorOf(Props(new Actor {
        override def receive: Receive = LoggingReceive {
          case CheckingAccountBalances(balances)    =>
            log.debug(s"Received checking account balances: $balances")
            checkingBalances = balances
            collectBalances
          case SavingsAccountBalances(balances)     =>
            log.debug(s"Received saving account balances: $balances")
            savingsBalances = balances
            collectBalances
          case MoneyMarketAccountBalances(balances) =>
            log.debug(s"Received Money Market balances: $balances")
            mmBalances = balances
            collectBalances
          case AccountRetrievalTimeout              =>
            sendResponseAndShutdown(AccountRetrievalTimeout)
        }

        def collectBalances: Unit =
          (checkingBalances, savingsBalances, mmBalances) match {
            case (Some(_), Some(_), Some(_)) =>
              log.debug("Value received for all three account types")
              timeoutMessager.cancel()
              sendResponseAndShutdown(AccountBalances(checkingBalances, savingsBalances, mmBalances))
            case _ =>
          }

        def sendResponseAndShutdown(response: Any): Unit = {
          originalSender ! response
          log.debug("Stopping context capturing actor")
          context.stop(self)
        }

        savingsAccounts ! GetCustomerAccountBalances(id)
        checkingAccounts ! GetCustomerAccountBalances(id)
        moneyMarketAccounts ! GetCustomerAccountBalances(id)

        import context.dispatcher

        val timeoutMessager: Cancellable = context.system.scheduler.scheduleOnce(250 milliseconds) {
          self ! AccountRetrievalTimeout
        }
      }))

    }

  }
}
