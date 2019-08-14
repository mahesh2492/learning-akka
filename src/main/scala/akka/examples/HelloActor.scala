package akka.examples

import akka.actor.{Actor, ActorSystem, Props}

class HelloActor(val name: String) extends Actor {

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