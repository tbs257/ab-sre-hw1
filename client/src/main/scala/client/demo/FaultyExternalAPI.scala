package client.demo

import cats.effect.kernel.{Async, Resource}
import cats.effect.std.Random
import cats.syntax.all._
import com.comcast.ip4s.Port
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Server
import sttp.model.StatusCode
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.{endpoint, statusCode, stringBody}

class FaultyExternalAPI[F[_]: Random: Async](successProbability: Double, blameUserProbability: Double) {

  def resource: Resource[F, Server] = {

    val httpApp = Http4sServerInterpreter[F]().toRoutes(serverEndpoint).orNotFound

    Resource.suspend(
      Port
        .fromInt(portInt)
        .liftTo[F](new RuntimeException("Invalid http port"))
        .map(port =>
          EmberServerBuilder
            .default[F]
            .withHost(host)
            .withPort(port)
            .withHttpApp(httpApp)
            .build,
        ),
    )
  }

  private val randomResponse: F[Either[(StatusCode, String), String]] = for {
    success  <- Random[F].nextDouble.map(_ < successProbability)
    response <- if (success) "hello".asRight[(StatusCode, String)].pure
                else
                  for {
                    blameUser       <- Random[F].nextDouble.map(_ < blameUserProbability)
                    (code, message) <- if (blameUser)
                                         Random[F]
                                           .elementOf(
                                             Set(
                                               StatusCode.BadRequest,
                                               StatusCode.Unauthorized,
                                               StatusCode.PaymentRequired,
                                               StatusCode.Forbidden,
                                             ),
                                           )
                                           .map((_, "user error"))
                                       else Random[F].elementOf(retryableCodes).map((_, "server error"))
                  } yield (code, message).asLeft[String]
  } yield response

  private val serverEndpoint =
    endpoint
      .post
      .in(path)
      .out(stringBody)
      .errorOut(statusCode)
      .errorOut(stringBody)
      .serverLogic(_ => randomResponse)
}

object FaultyExternalAPI {
  def make[F[_]: Async](
    successProbability: Double,
    blameUserProbability: Double,
  ): F[FaultyExternalAPI[F]] =
    Random
      .scalaUtilRandom
      .map(implicit random =>
        new FaultyExternalAPI[F](
          successProbability = successProbability,
          blameUserProbability = blameUserProbability,
        ),
      )
}
