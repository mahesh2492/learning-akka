package akka.examples.routing.pool

import akka.actor.{ActorSystem, Props}
import akka.examples.routing.common.Worker
import akka.examples.routing.common.Worker.Work
import akka.routing.RoundRobinPool

object RoundRobin extends App {

  val system = ActorSystem("round-robin-router")

  val routerPool = system.actorOf(RoundRobinPool(2).props(Props[Worker]), "round-robin-pool")

  routerPool ! Work()
  routerPool ! Work()
  routerPool ! Work()
  routerPool ! Work()

  Thread.sleep(10)

}
