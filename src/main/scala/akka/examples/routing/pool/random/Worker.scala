package akka.examples.routing.pool.random

import akka.actor.Actor
import akka.examples.routing.pool.random.Worker.Work

object Worker {

  case class Work()

}

class Worker extends Actor {

  override def receive: Receive = {
    case _: Work =>
      println(s"I received Work Message and My ActorRef: $self")
  }

}
