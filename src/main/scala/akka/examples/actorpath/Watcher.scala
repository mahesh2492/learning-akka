package akka.examples.actorpath

import akka.actor.{Actor, ActorIdentity, ActorLogging, ActorRef, ActorSelection}

class Watcher extends Actor with ActorLogging {

  var counterRef: ActorRef = _

  val selection: ActorSelection = context.actorSelection("/user/counter")

  selection ! None

  override def receive: Receive = {
    case ActorIdentity(_, Some(ref)) =>
      log.info(s"Actor Reference for counter is $ref")
    case ActorIdentity(_, None)      =>
      log.info(s"Actor Reference for counter does not live :(")
  }

}
