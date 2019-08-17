package service

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import effective.service.AccountBalanceRetriever
import effective.{AccountBalances, GetCustomerAccountBalances}
import org.scalatest.{BeforeAndAfterAll, MustMatchers, WordSpecLike}
import stubs.{CheckingAccountsProxyStub, MoneyMarketAccountsProxyStub, SavingsAccountsProxyStub}

import scala.concurrent.duration._

class AccountBalanceRetrieverTest extends TestKit(ActorSystem("AccountBalanceRetrieverTest")) with ImplicitSender
  with WordSpecLike with MustMatchers with BeforeAndAfterAll {

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "AccountBalanceRetriever" must {

    "return a list of account balances" in {
      val probe1 = TestProbe()
      val probe2 = TestProbe()

      val savingAccountProxy = system.actorOf(Props[SavingsAccountsProxyStub], name = "saving-account-proxy")
      val checkingAccountsProxy = system.actorOf(Props[CheckingAccountsProxyStub], name = "checking-accounts-proxy")
      val marketAccountsProxy = system.actorOf(Props[MoneyMarketAccountsProxyStub], name = "money-market-accounts-proxy")

      val accountsBalanceRetriever = system.actorOf(Props(
        new AccountBalanceRetriever(savingAccountProxy, checkingAccountsProxy, marketAccountsProxy)), name = "accounts-balance-retriever")

      within(300 milliseconds) {
        probe1.send(accountsBalanceRetriever, GetCustomerAccountBalances(1L))
        val result = probe1.expectMsgType[AccountBalances]
        println(">>>>>>>>>>>>>>>>>>>>>>>>>>>" + result)
        result must equal(List(AccountBalances(Some(List((3, 15000))), Some(List((1, 15000), (2, 29000))), Some(List()))))
      }

      within(300 milliseconds) {
        probe2.send(accountsBalanceRetriever, GetCustomerAccountBalances(2L))
        val result = probe1.expectMsgType[AccountBalances]
        println(">>>>>>>>>>>>>>>>>>>>>>>>>>>" + result)
        result must equal(List(AccountBalances(
          Some(List((6, 64000), (7, 1125000), (8, 40000))),
          Some(List((5, 80000))),
          Some(List((9, 640000), (10, 1125000), (11, 40000))))))
      }

    }
  }
}
