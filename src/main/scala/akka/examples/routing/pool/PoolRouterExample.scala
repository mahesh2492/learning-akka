package akka.examples.routing.pool

import akka.actor.{ActorSystem, Props}
import akka.examples.routing.pool.PoolRouter.Work

object PoolRouterExample extends App {

  val system = ActorSystem("router-example")
  val master = system.actorOf(Props[PoolRouter], name = "master")

  master ! Work()
  master ! Work()
  master ! Work()
  master ! Work()
  master ! Work()

  Thread.sleep(100)
  system.terminate()
}
