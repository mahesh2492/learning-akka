package akka.examples.actorpath

import akka.actor.{Actor, ActorIdentity, ActorRef, ActorSelection}

class Watcher extends Actor {

  var counterRef: ActorRef = _

  val selection: ActorSelection = context.actorSelection("/user/counter")

  selection ! None

  override def receive: Receive = {
    case ActorIdentity(_, Some(ref)) =>
      println(s"Actor Reference for counter is $ref")
    case ActorIdentity(_, None)      =>
      println(s"Actor Reference for counter does not live :(")
  }

}
