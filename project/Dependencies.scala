import sbt.*

object Dependencies {
  val all: Seq[ModuleID] = Seq(
    Libs.pureconfig,
    Libs.catsRetry,
    Libs.cats,
    Libs.http4s,
    Libs.tapir,
    Libs.sttp,
    Libs.fs2,
    Libs.logback,
    Libs.log4cats,
    Libs.enumeratum,
    Libs.circe,
    Libs.newtype,
    Libs.doobie,
    Libs.flyway,
    Libs.scalatest,
    Libs.testcontainers,
  ).flatten
}
