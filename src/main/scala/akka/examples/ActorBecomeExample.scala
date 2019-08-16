package akka.examples

import akka.actor.{Actor, ActorSystem, Props}

case object ActorNormalMessage

case object TryToFindSolution

case object BadGuysMakeAngry

class ActorBecomeExample extends Actor {

  import context._

  def angrySate: Receive = {
    case ActorNormalMessage =>
      println("Phew! I am going to be myself")
      become(normalState)
  }

  def normalState: Receive = {
    case TryToFindSolution =>
      println("Looking for solution to my problem..")
    case BadGuysMakeAngry  =>
      println("I am getting angry")
      become(angrySate)
  }

  override def receive: PartialFunction[Any, Unit] = {
    case BadGuysMakeAngry   => become(angrySate)
    case ActorNormalMessage => become(normalState)
  }

}

object ActorBecomeExampleTest extends App {
  val system = ActorSystem("ActorBecomeExampleTest")
  val actor = system.actorOf(Props[ActorBecomeExample], name = "actor-become-example")

  actor ! ActorNormalMessage
  actor ! TryToFindSolution
  actor ! BadGuysMakeAngry
  Thread.sleep(1000)
  actor ! ActorNormalMessage

  system.terminate()

}