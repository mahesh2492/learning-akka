package akka.examples.routing.common

import akka.actor.Actor
import akka.examples.routing.common.Worker.Work

object Worker {

  case class Work()

}

class Worker extends Actor {

  override def receive: Receive = {
    case _: Work =>
      println(s"I received Work Message and My ActorRef: $self")
  }

}
