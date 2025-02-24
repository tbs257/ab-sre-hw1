package client.demo

import cats.effect.Async
import cats.syntax.all._

import scala.concurrent.duration.FiniteDuration

class DemoApp[F[_]: Async](
  retryCount: Int,
  successProbability: Double,
  blameUserProbability: Double,
  requestCount: Int,
  useCircuitBreaker: Boolean,
  openTimeout: FiniteDuration,
  tryAgainDelay: FiniteDuration,
) {

  def demo: F[Unit] =
    FaultyExternalAPI
      .make[F](successProbability, blameUserProbability)
      .flatMap(
        _.resource
          .surround(
            new ClientDemo[F](
              retryCount = retryCount,
              requestCount = requestCount,
              useCircuitBreaker = useCircuitBreaker,
              openTimeout = openTimeout,
              tryAgainDelay = tryAgainDelay,
            ).run,
          ),
      )

}
