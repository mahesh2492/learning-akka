package service

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import effective.service.AccountBalanceRetriever
import effective.service.AccountBalanceRetriever.AccountRetrievalTimeout
import effective.{AccountBalances, GetCustomerAccountBalances}
import org.scalatest.{BeforeAndAfterAll, Matchers, Outcome, fixture}
import stubs.{CheckingAccountsProxyStub, MoneyMarketAccountsProxyStub, SavingsAccountsProxyStub, TimingOutSavingsAccountProxyStub}

import scala.concurrent.duration._

class AccountBalanceRetrieverTest extends TestKit(ActorSystem("AccountBalanceRetrieverTest")) with ImplicitSender
  with fixture.WordSpecLike with Matchers with BeforeAndAfterAll {

  type FixtureParam = TestFixture

  override def withFixture(test: OneArgTest): Outcome = {
    val fixture = new TestFixture
    super.withFixture(test.toNoArgTest(fixture))
  }

  class TestFixture {
    val probe1 = TestProbe()
    val probe2 = TestProbe()

    val savingAccountProxy: ActorRef =
      system.actorOf(Props[SavingsAccountsProxyStub], name = "saving-account-proxy" + java.util.UUID.randomUUID().toString)
    val checkingAccountsProxy: ActorRef =
      system.actorOf(Props[CheckingAccountsProxyStub], name = "checking-accounts-proxy" + java.util.UUID.randomUUID().toString)
    val marketAccountsProxy: ActorRef =
      system.actorOf(Props[MoneyMarketAccountsProxyStub], name = "money-market-accounts-proxy" + java.util.UUID.randomUUID().toString)
    val savingAccountTimeOut: ActorRef =
      system.actorOf(Props[TimingOutSavingsAccountProxyStub], name = "timing-out-saving-accounts-proxy" + java.util.UUID.randomUUID().toString)
    val accountsBalanceRetriever: ActorRef =
      system.actorOf(Props(new AccountBalanceRetriever(savingAccountProxy,
        checkingAccountsProxy, marketAccountsProxy)), name = "accounts-balance-retriever" + java.util.UUID.randomUUID().toString)

  }

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "AccountBalanceRetriever" should {

    "return a list of account balances" in { fixture =>
      import fixture._

      within(300 milliseconds) {
        probe1.send(accountsBalanceRetriever, GetCustomerAccountBalances(1L))
        val result = probe1.expectMsgType[AccountBalances]
        result should equal(AccountBalances(Some(List((3, 15000))), Some(List((1, 15000), (2, 29000))), Some(List())))
      }

      within(300 milliseconds) {
        probe2.send(accountsBalanceRetriever, GetCustomerAccountBalances(2L))
        val result = probe2.expectMsgType[AccountBalances]

        result should equal(AccountBalances(
          Some(List((6, 64000), (7, 1125000), (8, 40000))),
          Some(List((5, 80000))),
          Some(List((9, 640000), (10, 1125000), (11, 40000)))))
      }
    }

  "return a timeout while retrieving accounts balance when timeout is exceeded" in { fixture =>
    import fixture._

    val accountsBalanceRetriever = system.actorOf(Props(
      new AccountBalanceRetriever(savingAccountTimeOut, checkingAccountsProxy, marketAccountsProxy)),
      name = "accounts-balance-retriever" + java.util.UUID.randomUUID().toString)

    within(250 milliseconds, 500 milliseconds) {
      probe1.send(accountsBalanceRetriever, GetCustomerAccountBalances(1L))
      probe1.expectMsg(AccountRetrievalTimeout)
     }
    }
  }
}
