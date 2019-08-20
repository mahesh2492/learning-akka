package akka.examples.routing

import akka.actor.{ActorSystem, Props}
import akka.examples.routing.Worker.Work

object GroupRouterExample extends App {

  val worker =
    List(
      "/user/w1",
      "/user/w2",
      "/user/w3",
      "/user/w4",
      "/user/w5",
    )
  val system = ActorSystem("group-router-example")

  system.actorOf(Props[Worker], name = "w1")
  system.actorOf(Props[Worker], name = "w2")
  system.actorOf(Props[Worker], name = "w3")
  system.actorOf(Props[Worker], name = "w4")
  system.actorOf(Props[Worker], name = "w5")

  val masterGroup = system.actorOf(Props(classOf[MasterGroup], worker), name = "group-router")

  masterGroup ! Work()
  masterGroup ! Work()

  Thread.sleep(2000)
  system.terminate()
}
