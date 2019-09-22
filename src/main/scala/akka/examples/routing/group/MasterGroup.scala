package akka.examples.routing.group

import akka.actor.Actor
import akka.examples.routing.pool.random.Worker.Work

class MasterGroup(routerGroup: List[String]) extends Actor {

  override def receive: Receive = {
    case message: Work =>
      println(s"I am a group router and have received a message... $message")
      context.actorSelection(routerGroup(scala.util.Random.nextInt(routerGroup.size))) forward message
  }

}
