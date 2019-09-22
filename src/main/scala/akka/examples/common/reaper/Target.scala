package akka.examples.common.reaper

import java.util.Date

import akka.actor.{Actor, ActorSystem, PoisonPill, Props}
import akka.examples.common.{Reaper, ReaperWatched}

class Target extends Actor with ReaperWatched {
  override def receive: Receive = {
    case msg =>
      println(s"${new Date().toString} I received a message $msg")
    case _ => println("Got unknown message")
  }

}

object ShutDownApp extends App {

  val system = ActorSystem("reaper-pattern")

  val reaper = system.actorOf(Props[Reaper], Reaper.actorName)
  val target = system.actorOf(Props[Target], "target")

  target ! "Hello"
  target ! "Reaper Pattern"
  target ! PoisonPill

}
