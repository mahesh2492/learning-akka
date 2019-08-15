package akka.examples.stopping.actors

import akka.actor.{Actor, ActorRef, ActorSystem, PoisonPill, Props, Terminated}

class ChildActor extends Actor {
  override def receive: Receive = {
    case _ => println("Child has received a message")
  }
  override def postStop: Unit = println("ChildActor::postStop called")
}

class ParentWatch extends Actor {
  //starts an child actor and keep an eye on him
  val child: ActorRef = context.actorOf(Props[ChildActor], name = "ChildActor")
  context.watch(child)

  override def receive: PartialFunction[Any, Unit] = {
    case Terminated(_) => println("OMG! They have killed the child")
    case message: String   => println(s"Message has been received: $message")
  }
}

object DeathWatchExample extends App {
  val system = ActorSystem("DeathWatchExample")
  val parentWatch = system.actorOf(Props[ParentWatch], name = "ParentWatch")

  parentWatch ! "Hello"

  //find child and kill it

  val child = system.actorSelection("/user/ParentWatch/ChildActor")
  child ! PoisonPill

  system.terminate()
}