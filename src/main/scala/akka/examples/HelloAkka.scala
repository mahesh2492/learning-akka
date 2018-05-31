package akka.examples

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

//define an actor message
case class WhoToGreet(who: String)

//define greet actor

class Greeter extends Actor{

  def receive: PartialFunction[Any, Unit] = {
    case WhoToGreet(who) => println(s"Hello $who")
  }
}
object HelloAkka extends App{

 //create the  actor system
  val system = ActorSystem("Hello-Akka")

  //create the greet actor
  val greeter: ActorRef = system.actorOf(Props[Greeter], "greeter")

  //send WhoToGreet message to actor
  greeter ! WhoToGreet("Akka")

  //shutdown the actor system
  system.terminate()
}
