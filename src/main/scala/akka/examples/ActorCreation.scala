package akka.examples

import akka.actor.{Actor, ActorSystem, Props}
import akka.examples.MusicController.{Play, Stop}
import akka.examples.MusicPlayer.{StartMusic, StopMusic}

// Music controller messages
object MusicController {

  sealed trait ControllerMsg

  case object Play extends ControllerMsg

  case object Stop extends ControllerMsg

  def props: Props = Props[MusicController]

}

class MusicController extends Actor {

  override def receive: PartialFunction[Any, Unit] = {
    case Play =>
      println("Music has started.....")

    case Stop =>
      println("Music has stopped.....")
  }
}

// Music player messages
object MusicPlayer {

  sealed trait PlayMsg

  case object StopMusic extends PlayMsg

  case object StartMusic extends PlayMsg

}

// Music player
class MusicPlayer extends Actor {

  override def receive: PartialFunction[Any, Unit] = {
    case StopMusic  =>
      println("I don't want to stop the music ")
    case StartMusic =>
      val musicController = context.actorOf(MusicController.props, "music-controller")
      musicController ! Play
    case _          =>
      println("Unknown message")

  }
}

object ActorCreation extends App {

  // create the actor system
  val system = ActorSystem("actor-creation")

  //create the Music Player actor
  val musicPlayer = system.actorOf(Props[MusicPlayer], "music-player")

  //send the StartMusic message to actor
  musicPlayer ! StartMusic

  //shutdown the actor system
  system.terminate()
}
