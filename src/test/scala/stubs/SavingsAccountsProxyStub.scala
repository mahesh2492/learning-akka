package stubs

import akka.actor.ActorLogging
import akka.event.LoggingReceive
import effective.{GetCustomerAccountBalances, SavingAccountProxy, SavingsAccountBalances}

class SavingsAccountsProxyStub extends SavingAccountProxy with ActorLogging {

  val savingData = Map[Long, List[(Long, BigDecimal)]](
    1L -> List((1, 15000), (2, 29000)),
    2L -> List((5, 80000))
  )

  override def receive: Receive = LoggingReceive {

    case GetCustomerAccountBalances(id) =>
      log.debug(s"Received GetCustomerAccountBalances for Id: $id")
      savingData.get(id) match {
        case Some(data) => sender() ! SavingsAccountBalances(Some(data))
        case None       => sender() ! SavingsAccountBalances(Some(List()))
      }
  }
}
