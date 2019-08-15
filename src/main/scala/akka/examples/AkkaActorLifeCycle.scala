package akka.examples

import akka.actor.{Actor, ActorSystem, Props}

case object ForceRestart

class AkkaActorLifeCycle extends Actor {

  override def preStart(): Unit = println("Going to Start Actor")

  override def postStop(): Unit = println("AkkAActor: postStop")

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    println("AkkaActorLifeCycle: preRestart")
    println(s" MESSAGE: ${message.getOrElse("")}")
    println(s" REASON: ${reason.getMessage}")
    super.preRestart(reason, message)
  }

  override def postRestart(reason: Throwable): Unit = {
    println("AkkaActorLifeCycle: postRestart")
    println(s"reason: ${reason.getMessage}")
    super.postRestart(reason)
  }

  override def receive: PartialFunction[Any, Unit] = {
    case ForceRestart => throw new Exception("Boom!")
    case _            => println("Message Received!")
  }
}

object ActorLifeCycDemo extends App {
  val system = ActorSystem("ActorLifeCycDemo")
  val actor = system.actorOf(Props[AkkaActorLifeCycle], name = "AkkaActorLifeCycle")

  actor !  "Hello"
  Thread.sleep(1000)

  println("Restart the actor")
  actor ! ForceRestart

  Thread.sleep(1000)

  actor ! "actor is up again"

  //stop the actor
  system.stop(actor)

  //shutdown the actorsystem
  system.terminate()
}