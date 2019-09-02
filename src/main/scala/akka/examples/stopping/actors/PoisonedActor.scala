package akka.examples.stopping.actors

import akka.actor.{Actor, ActorSystem, PoisonPill, Props}

class PoisonedActor extends Actor {

  override def receive: Receive = {
    case message: String => println(s"Message has been received: $message")
    case _               => println("Got the unknown message!")
  }

  override def postStop: Unit = println("TestActor::postStop called")
}

object PoisonPillActor extends App {
  val system = ActorSystem("PoisonedActor")
  val poisonActor = system.actorOf(Props[PoisonedActor], name = "poison-pill")

  poisonActor ! "before poison pill"
  poisonActor ! "Hello"

  //poison pill
  poisonActor ! PoisonPill

  //the messages will never be processed
  poisonActor ! "after poison pill"

  system.terminate()
}
