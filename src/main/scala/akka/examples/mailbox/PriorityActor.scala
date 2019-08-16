package akka.examples.mailbox

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

class PriorityActor extends Actor with ActorLogging {

  override def receive: Receive = {
    case x: Int    => log.info("Message received: " + x)
    case x: String => log.info("Message received: " + x)
    case x: Long   => log.info("Message received: " + x)
    case default   => log.info("Message received: " + default)
  }

}


object PriorityActorExample extends App {
  
  val system = ActorSystem("priority-mailbox", ConfigFactory.load)
  val priorityActor = system.actorOf(Props[PriorityActor].withMailbox("priority-dispatcher"))

  priorityActor ! 6.0
  priorityActor ! 1
  priorityActor ! 5.0
  priorityActor ! 3
  priorityActor ! "Hello"
  priorityActor ! 5
  priorityActor ! "I am priority actor"
  priorityActor ! "I process string messages first,then integer, long and others"

  system.terminate()
}