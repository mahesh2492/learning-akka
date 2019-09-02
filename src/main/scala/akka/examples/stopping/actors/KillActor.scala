package akka.examples.stopping.actors

import akka.actor.{Actor, ActorSystem, Kill, Props}

class KillActor extends Actor {

  override def receive: Receive = {
    case message: String => println(s"Message has been received: $message")
    case _               => println("Got unknown message")
  }

  override def preStart(): Unit = println("Actor is live now")
  override def postStop(): Unit = println("Actor is no more live now")
}

object KillActorTest extends App {
  val system = ActorSystem("KillActorTest")
  val killActor = system.actorOf(Props[KillActor], name = "kill-actor-test")

  killActor ! "Hello! I am gonna kill you"
  killActor ! "Going to shoot the actor"
  killActor ! Kill
  killActor ! "Hey Actor! are you there?"
  system.terminate()

}
