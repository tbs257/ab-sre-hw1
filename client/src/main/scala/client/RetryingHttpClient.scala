package client

import cats._
import cats.effect._
import cats.syntax.all._
import cats.effect.syntax.all._
import client.errors.TryAgainLaterException
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.syntax._
import sttp.client3._
import sttp.model._

import scala.concurrent.duration._

class RetryingHttpClient[F[_]: MonadThrow: Temporal: Logger](
  sttpBackend: SttpBackend[F, Any],
  retryPolicy: RetryPolicy,
  circuitBreakerState: Ref[F, CircuitBreakerState],
  openTimeout:  FiniteDuration
) {

  def getData(url: String, doRetry: Boolean = true): F[String] =
    for {
      uri    <- Uri
                  .parse(url)
                  .leftMap(validationError => new IllegalArgumentException(validationError))
                  .liftTo[F]
      result <- if (doRetry) 0.tailRecM(retriesSoFar => sendRequest(uri) >>= makeRetryDecision(retriesSoFar))
                else sendRequest(uri).flatMap(_.body.leftMap(errorMessage => new Exception(errorMessage)).liftTo[F])
    } yield result

  def getDataWithCircuitBreaker(url: String): F[String] =
    circuitBreakerState.get.flatMap {
      case CircuitBreakerState.Closed   =>
        getData(url)
          .onError(e =>
            Logger[F].error(e)("Switching circuit breaker from closed to open state due to error: ") >>
              setOpenStateWithTimeout,
          )
      case CircuitBreakerState.Open     =>
        new TryAgainLaterException(
          "API not available (circuit breaker open due to too many failed requests).",
        ).raiseError
      case CircuitBreakerState.HalfOpen =>
        getData(url, doRetry = false).attemptTap {
          case Left(error) =>
            Logger[F].error(error)("Switching circuit breaker back to open state due to error: ") >>
              setOpenStateWithTimeout
          case _           =>
            info"API available again, switching circuit breaker back to closed state." >>
              circuitBreakerState.set(CircuitBreakerState.Closed)
        }
    }

  private def sendRequest(uri: Uri): F[Response[Either[String, String]]] =
    basicRequest
      .post(uri)
      .send(sttpBackend)

  private def makeRetryDecision(retriesSoFar: Int)(
    response: Response[Either[String, String]],
  ): F[Either[Int, String]] =
    response.body match {
      case Right(value)       => value.asRight[Int].pure
      case Left(errorMessage) =>
        for {
          _             <-
            if (retriesSoFar >= retryPolicy.retryCount)
              error"Retry limit of ${retryPolicy.retryCount} has been reached. Last error message was: $errorMessage" >>
                new Exception(errorMessage).raiseError[F, Unit]
            else ().pure
          retryableError = retryPolicy.retryableCodes.contains(response.code)
          _             <- if (retryableError) ().pure
                           else
                             Logger[F].error(
                               s"Got response with status code ${response.code} that isn't worth retrying. " +
                                 s"We only retry codes ${retryPolicy.retryableCodes.mkString("[", ",", "]")}. " +
                                 s"Error message was: $errorMessage",
                             ) >> new Exception(errorMessage).raiseError[F, Unit]
          delay          = if (retriesSoFar == 0) 0.seconds
                           else math.pow(2, retriesSoFar - 1).seconds
          _             <-
            warn"Got response with status code ${response.code} and error message: $errorMessage. Trying again in $delay."
          _             <- Temporal[F].sleep(delay)
        } yield (retriesSoFar + 1).asLeft
    }

  private val setOpenStateWithTimeout: F[Unit] = circuitBreakerState.set(CircuitBreakerState.Open) >>
    (
      Temporal[F].sleep(openTimeout) >>
        info"Switching circuit breaker to half open state due to timeout." >>
        circuitBreakerState.set(CircuitBreakerState.HalfOpen)
    ).start.void
}
