package akka.http.caching

import akka.actor.ActorSystem
import akka.http.caching.scaladsl.{Cache, CachingSettings}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object Application extends App {

  implicit val system: ActorSystem = ActorSystem("hello-akka-http")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val defaultCachingSettings = CachingSettings(system)
  val cache: Cache[Long, BigInt] = LfuCache[Long, BigInt]

  val route = new Factorial(cache).routes


  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")

  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}
