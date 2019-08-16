package akka.examples.dispatcher

import akka.actor.{Actor, ActorSystem, Props}

import scala.concurrent.{ExecutionContext, Future}

class BlockingFutureActor extends Actor {

  implicit val executionContext: ExecutionContext = context.dispatcher

  override def receive: Receive = {
    case i: Int =>
      println(s"Calling blocking future: $i")
      Future {
        Thread.sleep(5000)
        println(s"Blocking future finished $i")
      }
  }
}

class PrintActor extends Actor {
  def receive: PartialFunction[Any, Unit] = {
    case i: Int =>
      println(s"PrintActor: $i")
  }
}


object BlockingExample extends App {

  val system = ActorSystem("Blocking-Example")
  val actor1 = system.actorOf(Props(new BlockingFutureActor))
  val actor2 = system.actorOf(Props(new PrintActor))

  for (i <- 1 to 150) {
    actor1 ! i
    actor2 ! i
  }

  system.terminate()
}