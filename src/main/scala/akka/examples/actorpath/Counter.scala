package akka.examples.actorpath

import akka.actor.Actor
import akka.examples.actorpath.Counter.{Dec, Inc}

object Counter {

  final case class Inc(x: Int)
  final case class Dec(x: Int)
}

class Counter extends Actor {

  var count = 0

  override def receive: Receive = {
    case Inc(x) => count += x
    case Dec(x) => count -= x
  }

}