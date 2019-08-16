package akka.examples.stopping.actors

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, PoisonPill, Props, Terminated}

class ChildActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case _ => log.info("Child has received a message")
  }

  override def postStop: Unit = log.info("ChildActor::postStop called")
}

class ParentWatch extends Actor with ActorLogging {
  //starts an child actor and keeps an eye on him
  val child: ActorRef = context.actorOf(Props[ChildActor], name = "ChildActor")
  context.watch(child)

  override def receive: PartialFunction[Any, Unit] = {
    case Terminated(_)   => log.info("OMG! They have killed the child")
    case message: String => log.info(s"Message has been received: $message")
  }
}

object DeathWatchExample extends App {
  val system = ActorSystem("DeathWatchExample")
  val parentWatch = system.actorOf(Props[ParentWatch], name = "parent")

  parentWatch ! "Hello"

  //find child and kill it
  val child = system.actorSelection("/user/parent/ChildActor")
  child ! PoisonPill

  system.terminate()
}