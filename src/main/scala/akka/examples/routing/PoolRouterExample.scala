package akka.examples.routing

import akka.actor.{ActorSystem, Props}
import akka.examples.routing.Worker.Work

object PoolRouterExample extends App {

  val system = ActorSystem("router-example")
  val master = system.actorOf(Props[MasterPool], name = "master")

  master ! Work()
  master ! Work()
  master ! Work()
  master ! Work()
  master ! Work()

  Thread.sleep(100)
  system.terminate()
}
