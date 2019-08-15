package akka.examples.stopping.actors

import akka.actor.{Actor, ActorSystem, Props}
import akka.pattern.gracefulStop

import scala.concurrent.Await
import scala.concurrent.duration._

class GraceFullStopActor extends Actor {

  override def receive: Receive = {
    case message: String => println(s"Message has been receivec: $message")
    case _               => println("Got unknown message")
  }

  override def postStop(): Unit = println("GraceFullStopActor: postStop has been called")
}

object GraceFullStopTest extends App {
  val system = ActorSystem("GraceFullStopActor")
  val graceFullStopActor = system.actorOf(Props[GraceFullStopActor], name = "GraceFullStopTest")

  graceFullStopActor ! "Going to kill you"
  /*
  If the order in which actors are terminated is important, using gracefulStop can be a
  good way to attempt to terminate them in a desired order.
   */
  try {
    val stopped = gracefulStop(graceFullStopActor, 2 seconds)
    Await.result(stopped, 3 seconds)
    println("testActor was stopped")
  } catch {
    case e: Exception => e.printStackTrace()
  } finally {
    system.terminate()
  }

}