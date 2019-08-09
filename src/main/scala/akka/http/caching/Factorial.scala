package akka.http.caching

import akka.actor.ActorSystem
import akka.http.caching.scaladsl.Cache
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

import scala.concurrent.Future

class Factorial(cache: Cache[Long, BigInt])(implicit system: ActorSystem, materializer: ActorMaterializer) {

  def factorial(number: Long): Future[BigInt] = {
    def fact(number: BigInt, result: BigInt): BigInt = {
      if (number == 1) result
      else
        fact(number - 1, result * number)
    }

    Future.successful(fact(number, 1))
  }

  def time[R](block: => R): R = {
    val t0 = System.currentTimeMillis()
    val result = block // call-by-name
    val t1 = System.currentTimeMillis()
    println("Elapsed time: " + (t1 - t0) / 1000 + " secs")
    result
  }

  val routes: Route = path("factorial" / LongNumber) { number =>
    val result = time(cache.getOrLoad(number, _ => factorial(number)))
    complete(HttpResponse(StatusCodes.OK, entity = result.toString))
  }

}
