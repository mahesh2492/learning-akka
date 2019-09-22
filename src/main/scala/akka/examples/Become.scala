package akka.examples

import akka.actor.{Actor, ActorSystem, Props, Stash}
import akka.examples.UserStorage.{Connect, DbOperation, Disconnect, Operation}

case class User(name: String, email: String)

object UserStorage {
  trait DbOperation

  object DbOperation {
    case object Create extends DbOperation
    case object Update extends DbOperation
    case object Read extends DbOperation
    case object Delete extends DbOperation
  }

  case object Connect
  case object Disconnect

  case class Operation(dbOperation: DbOperation, user: Option[User])

}

class UserStorage extends Actor with Stash {

  def receive: Receive = disconnected

  def connected: Actor.Receive =  {
    case Disconnect =>
      println("User Storage disconnected to DB")
      context.unbecome()
    case Operation(op, user) =>
      println(s"User Storage receive $op to do in user: $user")
  }

  def disconnected: Actor.Receive = {
    case Connect =>
      println("User Storage connected to DB")
      unstashAll()
      context.become(connected)
    case _ =>
      stash()
  }

}

object BecomeHotSwap extends App {

  val system = ActorSystem("Hotswap-become")

  val userStorage = system.actorOf(Props[UserStorage], "user-storage")

  userStorage ! Connect

  userStorage ! Operation(DbOperation.Create, Some(User("mahesh", "mahesh.kndpl@gmail.com")))

  userStorage ! Disconnect

  Thread.sleep(200)

  system.terminate()
}