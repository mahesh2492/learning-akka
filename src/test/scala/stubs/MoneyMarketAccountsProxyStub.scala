package stubs

import akka.actor.ActorLogging
import akka.event.LoggingReceive
import effective.{GetCustomerAccountBalances, MoneyMarketAccountBalances, MoneyMarketAccountsProxy}

class MoneyMarketAccountsProxyStub extends MoneyMarketAccountsProxy with ActorLogging {

  val accountData = Map[Long, List[(Long, BigDecimal)]](
    2L -> List((9, 640000), (10, 1125000), (11, 40000)))

  def receive = LoggingReceive {
    case GetCustomerAccountBalances(id) =>
      log.debug(s"Received GetCustomerAccountBalances for Id: $id")
      accountData.get(id) match {
        case Some(data) => sender() ! MoneyMarketAccountBalances(Some(data))
        case None       => sender() ! MoneyMarketAccountBalances(Some(List()))
      }
  }
}
