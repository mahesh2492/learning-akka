package akka.examples.basic

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}

class HelloActor(val name: String) extends Actor with ActorLogging {

  override def receive: Receive = {
    case "hello" => println(s"Hello $name")
    case _       => println(s"huh? $name")
  }
}

object Main extends App {
  val system = ActorSystem("hello-actor")
  val helloActor = system.actorOf(Props(new HelloActor("akka")), "hello")

  helloActor ! "hello"
  helloActor ! "hola"

  system.terminate()
}