package akka.examples.hotswap_behavior

import akka.actor.{ActorSystem, FSM, Props, Stash}
import UserStorageFsm._

object UserStorageFsm {

  sealed trait State
  case object Connected extends State
  case object Disconnected extends State

  sealed trait Data
  case object EmptyData extends Data

  trait DbOperation
  object DbOperation {
    case object Create extends DbOperation
    case object Update extends DbOperation
    case object Read extends DbOperation
    case object Delete extends DbOperation
  }

  case object Connect
  case object Disconnect
  case class User(name: String, email: String)

  case class Operation(dbOperation: DbOperation, user: Option[User])

}

class UserStorageFsm extends FSM[UserStorageFsm.State, UserStorageFsm.Data] with Stash {

  startWith(Disconnected, EmptyData)

  when(Disconnected) {
    case Event(Connect,_) =>
      println("User Storage connected to DB")
      unstashAll()
      goto(Connected) using EmptyData

    case Event(_, _) =>
       stash()
      stay() using EmptyData
  }

  when(Connected) {
    case Event(Disconnect,_) =>
      println("User Storage disconnected from DB")
      goto(Disconnected) using EmptyData

    case Event(Operation(op, user), _) =>
      println(s"User Storage receives $op operation to do in user: $user")
      stay() using EmptyData
  }
  initialize()

}

object FsmBehavior extends App {

  val system = ActorSystem("fsm-become")

  val userStorage = system.actorOf(Props[UserStorageFsm], "user-storage")

  userStorage ! Connect

  userStorage ! Operation(DbOperation.Create, Some(User("mahesh", "mahesh.kndpl@gmail.com")))

  userStorage ! Disconnect

  Thread.sleep(200)

  system.terminate()

}
