package akka.examples.routing.pool.random

import akka.actor.{ActorSystem, Props}
import akka.examples.routing.pool.random.Worker.Work
import akka.routing.FromConfig

object RandomPoolRouter extends App {

  val system = ActorSystem("random-router")
  val routerPool = system.actorOf(FromConfig.props(Props[Worker]), name = "random-router-pool")

  routerPool ! Work()
  routerPool ! Work()
  routerPool ! Work()
  routerPool ! Work()
  routerPool ! Work()

  Thread.sleep(1000)
  system.terminate()
}
