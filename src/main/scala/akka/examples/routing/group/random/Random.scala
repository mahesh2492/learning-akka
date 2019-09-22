package akka.examples.routing.group.random

import akka.actor.{ActorSystem, Props}
import akka.examples.routing.common.Worker
import akka.examples.routing.common.Worker.Work
import akka.routing.RandomGroup

object Random extends App {

  val system = ActorSystem("random-router")

  system.actorOf(Props[Worker], "w1")
  system.actorOf(Props[Worker], "w2")
  system.actorOf(Props[Worker], "w3")

  val paths = List("/user/w1", "/user/w2", "/user/w3")

  val routerGroup = system.actorOf(RandomGroup(paths).props(),  "random-router-group")

  routerGroup ! Work()
  routerGroup ! Work()
  routerGroup ! Work()
  routerGroup ! Work()

  Thread.sleep(2000)

  system.terminate()

}
