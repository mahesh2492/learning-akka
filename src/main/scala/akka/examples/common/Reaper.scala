package akka.examples.common

import akka.actor.{Actor, ActorLogging, ActorRef, Terminated}
import akka.examples.common.Reaper.WatchMe

import scala.collection.mutable.ListBuffer

object Reaper {
  val actorName = "reaper"

  //used by other actors to watch me
  case class WatchMe(actor: ActorRef)

}

class Reaper extends Actor with ActorLogging {

  val watched: ListBuffer[ActorRef] = ListBuffer.empty[ActorRef]

  private def allSoulsReaped() = {
    println("Shutting Down System has started!!")
    context.system.terminate()
    println("Shutting Down System has ended!!")
  }

  override def receive: Receive = {
    case WatchMe(ref) =>
      context.watch(ref)
      watched += ref

    case Terminated(ref) =>
      watched -= ref
      if (watched.isEmpty) allSoulsReaped()

    case _ => "Unknown message"
  }

}

trait ReaperWatched {
  this: Actor =>
  override def preStart(): Unit = {
    println("In Pre Start!!")
    context.actorSelection("/user/" + Reaper.actorName) ! Reaper.WatchMe(self)
  }
}
