package akka.examples.supervisions

import akka.actor.SupervisorStrategy.{Escalate, Restart, Resume, Stop}
import akka.actor.{Actor, ActorRef, ActorSystem, OneForOneStrategy, Props}
import akka.examples.supervisions.Aphrodite.{RestartException, ResumeException, StopException}

import scala.concurrent.duration._

object Aphrodite {

  case object ResumeException extends Exception

  case object StopException extends Exception

  case object RestartException extends Exception

}

class Aphrodite extends Actor {

  override def preStart(): Unit =
    println("Aphrodite: preStart...")

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    println("Aphrodite: preRestart...")
    super.preRestart(reason, message)
  }

  override def postRestart(reason: Throwable): Unit = {
    println("Aphrodite: postRestart...")
    super.postRestart(reason)
  }

  override def postStop(): Unit =
    println("Aphrodite: postStop...")

  override def receive: Receive = {
    case "Resume"  => throw ResumeException
    case "Stop"    => throw StopException
    case "Restart" => throw RestartException
    case _         => throw new Exception
  }
}

class Hera extends Actor {

  var childActor: ActorRef = _

  override val supervisorStrategy: OneForOneStrategy =
    OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 seconds) {
      case ResumeException  => Resume
      case RestartException => Restart
      case StopException    => Stop
      case _: Exception     => Escalate
    }

  override def preStart(): Unit = {
    childActor = context.actorOf(Props[Aphrodite], name = "aphrodite")
    Thread.sleep(100)
  }

  override def receive: Receive = {
    case message: String =>
      println(s"Message has been received by Hera: $message")
      childActor ! message
  }

}

object Supervision extends App {

  val system = ActorSystem("Supervision")
  val hera = system.actorOf(Props[Hera], name = "hera")

  hera ! "Resume"
  Thread.sleep(1000)

  hera ! "Restart"
  Thread.sleep(1000)

  hera ! "Stop"
  Thread.sleep(1000)

  system.terminate()
}
