package akka.examples.actorpath

import akka.actor.{ActorSystem, Props}

object ActorSelectionWatcher extends App {

  val system = ActorSystem("actor-selection-watcher")
  val watcher = system.actorOf(Props[Watcher], name = "watcher")
  val counter = system.actorOf(Props[Counter], name = "counter")

  Thread.sleep(1000)
  system.terminate()
}
