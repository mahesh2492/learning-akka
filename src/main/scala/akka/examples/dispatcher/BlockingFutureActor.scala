package akka.examples.dispatcher

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}

import scala.concurrent.{ExecutionContext, Future}

class BlockingFutureActor extends Actor with ActorLogging {

  implicit val executionContext: ExecutionContext = context.dispatcher

  override def receive: Receive = {
    case i: Int =>
      log.info(s"Calling blocking future: $i")
      Future {
        Thread.sleep(5000)
        log.info(s"Blocking future finished $i")
      }
  }
}

class PrintActor extends Actor with ActorLogging {
  def receive: PartialFunction[Any, Unit] = {
    case i: Int =>
      log.info(s"PrintActor: $i")
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

}