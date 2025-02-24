package client.demo

import cats.effect.{IO, IOApp}

import scala.concurrent.duration._

object Task2DemoApp
    extends DemoApp[IO](
      retryCount = 3,
      successProbability = 0.4,
      blameUserProbability = 0.2,
      requestCount = 20,
      useCircuitBreaker = true,
      openTimeout = 10.seconds,
      tryAgainDelay = 6.seconds,
    )
    with IOApp.Simple {
  override def run: IO[Unit] = demo
}
