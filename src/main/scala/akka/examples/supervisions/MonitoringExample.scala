package akka.examples.supervisions

import akka.actor.{Actor, ActorRef, ActorSystem, Props, Terminated}

class Ares(athena: ActorRef) extends Actor {

  override def preStart(): Unit = println("Ares: preStart...")

  override def postStop(): Unit = println("Ares: postStart...")

  override def receive: PartialFunction[Any, Unit] = {
    case Terminated(_) =>
      println("Ares received Terminated")
      context.stop(self)
  }
}

class Athena extends Actor {
  override def receive: PartialFunction[Any, Unit] = {
    case message =>
      println(s"Athena received $message")
      context.stop(self)
  }
}

object MonitoringExample extends App {

  val system = ActorSystem("MonitoringExample")
  val athena = system.actorOf(Props[Athena], name = "athena")
  val ares = system.actorOf(Props(classOf[Ares], athena), name = "ares")

  athena ! "Hi"

  system.terminate()
}
