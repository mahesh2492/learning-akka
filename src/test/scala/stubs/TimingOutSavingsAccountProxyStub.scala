package stubs

import akka.actor.ActorLogging
import akka.event.LoggingReceive
import effective.{GetCustomerAccountBalances, SavingAccountProxy}

class TimingOutSavingsAccountProxyStub extends SavingAccountProxy with ActorLogging {
  def receive = LoggingReceive {
    case GetCustomerAccountBalances(_) =>
      log.debug(s"Forcing timeout by not responding!")
  }
}