package akka.examples

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.examples.Checker.{BlackUser, CheckUser, WhiteUser}
import akka.pattern.ask
import akka.examples.Recorder.NewUser
import akka.examples.Storage.AddUser
import akka.util.Timeout

import scala.concurrent.duration._
/*
This is an example for registration  of new user. We have three main components here i.e recorder, checker and storage.
Add request go to recorder. Recorder will check whether new user is black listed or not.
 And Recorder will add user to storage if user is not black listed.
 */
case class User(name: String, email: String)

object Recorder{
  sealed trait RecorderMsg
  // Recorder Messages
  case class NewUser(user: User) extends RecorderMsg

  def props(checker: ActorRef, storage: ActorRef): Props =
    Props(new Recorder(checker, storage))
}

object Checker{
  sealed trait CheckerMsg
  //Checker Messages
  case class CheckUser(user: User) extends CheckerMsg

  sealed trait CheckerResponse
  //Checker Response
  case class BlackUser(user: User) extends CheckerResponse
  case class WhiteUser(user: User) extends CheckerResponse
}

object Storage{
  sealed trait StorageMsg
 //Storage Messages
  case class AddUser(user: User) extends StorageMsg
}

class Storage extends Actor{

  val users = new scala.collection.mutable.ListBuffer[User]

  override def receive: PartialFunction[Any, Unit] = {
    case AddUser(user) =>
      println(s"Storage: $user added")
      users += user
    case _ => println("default request")
  }
}

class Checker extends Actor{

  val blackList = List(
    User("Dawood", "dawood.ak47@gmail.com")
  )

  override def receive: PartialFunction[Any, Unit] = {
    case CheckUser(user) if blackList.contains(user) =>
      println(s"Checker: $user is in the blacklist")
      sender() ! BlackUser(user)
    case CheckUser(user) =>
      println(s"Checker: $user is not in the blacklist")
      sender() ! WhiteUser(user)
  }
}

class Recorder(checker: ActorRef, storage: ActorRef) extends Actor{
  import scala.concurrent.ExecutionContext.Implicits.global

  implicit val timeout: Timeout = Timeout(5 seconds)

  override def receive: PartialFunction[Any, Unit] = {
    case NewUser(user) =>
      checker ? CheckUser(user) map{
        case WhiteUser(`user`) => println(s"user... $user")
          storage ! AddUser(user)
        case BlackUser(`user`) =>
          println(s"Recorder: $user is in the blacllist")
      }
  }
}

object Registration extends App{

  //create the actor system
  val system = ActorSystem("register-user")

  //create the checker actor
  val checker: ActorRef = system.actorOf(Props[Checker], "checker")

  //create the storage actor
  val storage: ActorRef = system.actorOf(Props[Storage], "storage")

  //create the recorder actor
  val recorder: ActorRef = system.actorOf(Recorder.props(checker, storage), "recorder")

  //send NewUser message to recorder
  recorder ! Recorder.NewUser(User("Mahesh", "mahesh.kndpl@gmail.com"))

  //shutdown the system
  system.terminate()
}
