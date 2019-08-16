package akka.examples.dispatcher

import akka.actor.{Actor, ActorSystem, Props}

import scala.concurrent.{ExecutionContext, Future}


class SeparateDispatcherFutureActor extends Actor {

  /*
  One of the most efficient methods of isolating the blocking behavior such that it does not impact the rest
  of the system is to prepare and use a dedicated dispatcher for all those blocking operations.
  This technique is often referred to as as “bulk-heading” or simply “isolating blocking”.
   */
  implicit val executionContext: ExecutionContext =
    context.system.dispatchers.lookup("my-blocking-dispatcher")

  override def receive: Receive = {
    case i: Int =>
      println(s"Calling blocking future: $i")
      Future {
        Thread.sleep(2000)
        println(s"Blocking future finished $i")
      }
  }

}

object SeparateDispatcherExample extends App {

  val system = ActorSystem("separate-dispatcher")
  val actor1 = system.actorOf(Props[BlockingFutureActor])

  for(i <- 1 to 100)
    actor1 ! i

}