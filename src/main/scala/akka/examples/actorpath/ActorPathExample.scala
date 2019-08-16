package akka.examples.actorpath

import akka.actor.{ActorSystem, PoisonPill, Props}

object ActorPathExample extends App {

  val system = ActorSystem("watch-actor-selection")
  val counter = system.actorOf(Props[Counter], "watcher")
  println(s"Actor Reference for counter: $counter")

  val counterSelection = system.actorSelection("counter")
  println(s"Actor Selection for counter: $counterSelection")

  counter ! PoisonPill

  Thread.sleep(1000)

  val counter2 = system.actorOf(Props[Counter], "watcher")

  println(s"Actor Reference for counter: $counter2")

  val counterSelection2 = system.actorSelection("counter")
  println(s"Actor Selection for counter: $counterSelection2")



  system.terminate()
}
