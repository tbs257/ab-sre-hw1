package client.demo

import cats.effect.{IO, IOApp}

import scala.concurrent.duration._

object Task1DemoApp
    extends DemoApp[IO](
      retryCount = 3,
      successProbability = 0.3,
      blameUserProbability = 0.3,
      requestCount = 20,
      useCircuitBreaker = false,
      openTimeout = 0.seconds,
      tryAgainDelay = 0.seconds,
    )
    with IOApp.Simple {
  override def run: IO[Unit] = demo
}
