package stubs

import akka.actor.ActorLogging
import akka.event.LoggingReceive
import effective.{CheckingAccountBalances, CheckingAccountsProxy, GetCustomerAccountBalances}

class CheckingAccountsProxyStub extends CheckingAccountsProxy with ActorLogging {

  val accountsData = Map[Long, List[(Long, BigDecimal)]](
    1L -> List((3, 15000)),
    2L -> List((6, 64000), (7, 1125000), (8, 40000))
  )

  override def receive: Receive = LoggingReceive {

    case GetCustomerAccountBalances(id) =>
      log.debug(s"Received GetCustomerAccountBalances for Id: $id")
      accountsData.get(id) match {
        case Some(data) => sender() ! CheckingAccountBalances(Some(data))
        case None       => sender ! CheckingAccountBalances(Some(List()))
      }
  }
}
