package akka.examples.routing.pool

import akka.actor.{Actor, ActorRef, Props}
import akka.examples.routing.pool.PoolRouter.Work
import akka.examples.routing.pool.random.Worker

object PoolRouter {
  case class Work()
}

class PoolRouter extends Actor {

  var routees: List[ActorRef] = _

  override def preStart(): Unit = {
    routees = List.fill(5)(
      context.actorOf(Props[Worker])
    )
  }

  override def receive: Receive =  {
    case message : Work =>
      println("I am a router and have received a message...")
      routees(util.Random.nextInt(routees.size)) forward(message)
  }
}
