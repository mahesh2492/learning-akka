/*
package akka.http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object WebServer extends App {

  implicit val system: ActorSystem = ActorSystem("hello-akka-http")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val route =
    path("hello"){
     get{
       complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
     }
    }

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")

  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}
*/


import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.Await
import scala.util.{Failure, Success}

object FuturesExample extends App {

    val f = Future {
      // Doing some job
    }

    val f1 = Future {
      // Doing some job
    }

    f.onComplete {
      case Success(value) => println(">>>>>>>>>>>>>>>>>>>>>>>>>>Done")
      case Failure(e) =>
        println("==========================================")
        e.printStackTrace
    }

}