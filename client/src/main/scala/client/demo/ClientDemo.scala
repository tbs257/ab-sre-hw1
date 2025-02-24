package client.demo

import cats.effect.{Async, Ref, Temporal}
import cats.syntax.all._
import client.errors.TryAgainLaterException
import client.{CircuitBreakerState, RetryPolicy, RetryingHttpClient}
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.syntax._
import sttp.client3.http4s.Http4sBackend

import scala.concurrent.duration.FiniteDuration

class ClientDemo[F[_]: Async](
  retryCount: Int,
  requestCount: Int,
  useCircuitBreaker: Boolean,
  openTimeout: FiniteDuration,
  tryAgainDelay: FiniteDuration,
) extends {

  private val faultyExternalApiUrl = s"http://$host:$portInt/$path"

  def run: F[Unit] =
    Http4sBackend.usingDefaultEmberClientBuilder().use { backend =>
      for {
        implicit0(logger: Logger[F]) <- Slf4jLogger.create
        retryPolicy                   = RetryPolicy(
                                          retryCount = retryCount,
                                          retryableCodes = retryableCodes,
                                        )
        circuitBreakerState          <- Ref.of[F, CircuitBreakerState](CircuitBreakerState.Closed)
        client                        = new RetryingHttpClient[F](
                                          sttpBackend = backend,
                                          retryPolicy = retryPolicy,
                                          circuitBreakerState = circuitBreakerState,
                                          openTimeout = openTimeout,
                                        )
        _                            <- getDataFromFaultyExternalApi(client)
                                          .redeemWith(
                                            recover = {
                                              case _: TryAgainLaterException =>
                                                info"Circuit breaker open, trying again in $tryAgainDelay." >>
                                                  Temporal[F].sleep(tryAgainDelay)
                                              case t                         =>
                                                Logger[F].error(t)("Error performing request to the external api.")
                                            },
                                            bind = result => info"Successfully performed request to the external api. Got response: $result",
                                          )
                                          .replicateA_(requestCount)
      } yield ()
    }

  private def getDataFromFaultyExternalApi(client: RetryingHttpClient[F]) =
    if (useCircuitBreaker) client.getDataWithCircuitBreaker(faultyExternalApiUrl)
    else client.getData(faultyExternalApiUrl)
}
