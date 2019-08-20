package akka.examples.routing

import akka.actor.Actor
import akka.examples.routing.Worker.Work

class MasterGroup(routerGroup: List[String]) extends Actor {

  override def receive: Receive = {
    case message: Work =>
      println("I am a group router and have received a message...")
      context.actorSelection(routerGroup(scala.util.Random.nextInt(routerGroup.size))) forward message
  }
}
