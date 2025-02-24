import sbt.*

object Libs {

  private object Versions {
    val pureconfigVersion       = "0.17.5"
    val catsRetryVersion        = "3.1.3"
    val catsCoreVersion         = "2.10.0"
    val catsEffectVersion       = "3.5.3"
    val catsTaglessVersion      = "0.15.0"
    val http4sVersion           = "0.23.25"
    val tapirVersion            = "1.9.9"
    val sttpVersion             = "3.9.3"
    val fs2Version              = "3.3.1"
    val s3Version               = "2.25.35"
    val logbackVersion          = "1.4.14"
    val log4catsVersion         = "2.6.0"
    val prometheusVersion       = "0.16.0"
    val enumeratumVersion       = "1.7.3"
    val enumeratumDoobieVersion = "1.7.4"
    val circeVersion            = "0.14.6"
    val phobosVersion           = "0.21.0"
    val newtypeVersion          = "0.4.4"
    val doobieVersion           = "1.0.0-RC4"
    val flywayVersion           = "9.17.0"
    val redis4catsVersion       = "1.5.2"
    val scalatestVersion        = "3.2.18"
    val testcontainersVersion   = "0.41.3"
  }

  val pureconfig: Seq[ModuleID] = Seq(
    "com.github.pureconfig" %% "pureconfig" % Versions.pureconfigVersion,
  )

  val catsRetry: Seq[ModuleID] = Seq(
    "com.github.cb372" %% "cats-retry" % Versions.catsRetryVersion,
  )

  val cats: Seq[ModuleID] = Seq(
    "org.typelevel" %% "cats-core"           % Versions.catsCoreVersion,
    "org.typelevel" %% "cats-effect"         % Versions.catsEffectVersion,
    "org.typelevel" %% "cats-tagless-macros" % Versions.catsTaglessVersion,
  )

  val http4s: Seq[ModuleID] = Seq(
    "org.http4s" %% "http4s-ember-client" % Versions.http4sVersion,
    "org.http4s" %% "http4s-ember-server" % Versions.http4sVersion,
    "org.http4s" %% "http4s-dsl"          % Versions.http4sVersion,
    "org.http4s" %% "http4s-circe"        % Versions.http4sVersion,
  )

  val tapir: Seq[ModuleID] = Seq(
    "com.softwaremill.sttp.tapir" %% "tapir-http4s-server"      % Versions.tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-json-circe"         % Versions.tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-newtype"            % Versions.tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-enumeratum"         % Versions.tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-prometheus-metrics" % Versions.tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle"  % Versions.tapirVersion,
  )

  val sttp: Seq[ModuleID] = Seq(
    "com.softwaremill.sttp.client3" %% "core"                           % Versions.sttpVersion,
    "com.softwaremill.sttp.client3" %% "circe"                          % Versions.sttpVersion,
    "com.softwaremill.sttp.client3" %% "async-http-client-backend-cats" % Versions.sttpVersion,
    "com.softwaremill.sttp.client3" %% "http4s-backend"                 % Versions.sttpVersion,
    "com.softwaremill.sttp.client3" %% "prometheus-backend"             % Versions.sttpVersion,
  )

  val fs2: Seq[ModuleID] = Seq(
    "com.github.fd4s" %% "fs2-kafka" % Versions.fs2Version,
  )

  val s3: Seq[ModuleID] = Seq(
    "software.amazon.awssdk" % "s3" % Versions.s3Version,
  )

  val logback: Seq[ModuleID] = Seq(
    "ch.qos.logback" % "logback-classic" % Versions.logbackVersion,
  )

  val log4cats: Seq[ModuleID] = Seq(
    "org.typelevel" %% "log4cats-core"  % Versions.log4catsVersion,
    "org.typelevel" %% "log4cats-slf4j" % Versions.log4catsVersion,
  )

  val prometheus: Seq[ModuleID] = Seq(
    "io.prometheus" % "simpleclient_common"  % Versions.prometheusVersion,
    "io.prometheus" % "simpleclient_hotspot" % Versions.prometheusVersion,
  )

  val enumeratum: Seq[ModuleID] = Seq(
    "com.beachape" %% "enumeratum"        % Versions.enumeratumVersion,
    "com.beachape" %% "enumeratum-circe"  % Versions.enumeratumVersion,
    "com.beachape" %% "enumeratum-doobie" % Versions.enumeratumDoobieVersion,
  )

  val circe: Seq[ModuleID] = Seq(
    "io.circe" %% "circe-generic"        % Versions.circeVersion,
    "io.circe" %% "circe-generic-extras" % "0.14.3",
    "io.circe" %% "circe-parser"         % Versions.circeVersion,
  )

  val phobos: Seq[ModuleID] = Seq(
    "ru.tinkoff" %% "phobos-core" % Versions.phobosVersion,
  )

  val newtype: Seq[ModuleID] = Seq(
    "io.estatico" %% "newtype" % Versions.newtypeVersion,
  )

  val doobie: Seq[ModuleID] = Seq(
    "org.tpolecat" %% "doobie-core"           % Versions.doobieVersion,
    "org.tpolecat" %% "doobie-hikari"         % Versions.doobieVersion,
    "org.tpolecat" %% "doobie-postgres"       % Versions.doobieVersion,
    "org.tpolecat" %% "doobie-postgres-circe" % Versions.doobieVersion,
  )

  val flyway: Seq[ModuleID] = Seq(
    "org.flywaydb" % "flyway-core" % Versions.flywayVersion,
  )

  val redis4cats: Seq[ModuleID] = Seq(
    "dev.profunktor" %% "redis4cats-effects"  % Versions.redis4catsVersion,
    "dev.profunktor" %% "redis4cats-log4cats" % Versions.redis4catsVersion,
  )

  val scalatest: Seq[ModuleID] = Seq(
    "org.scalatest" %% "scalatest" % Versions.scalatestVersion,
  )

  val testcontainers: Seq[ModuleID] = Seq(
    "com.dimafeng" %% "testcontainers-scala-scalatest"  % Versions.testcontainersVersion,
    "com.dimafeng" %% "testcontainers-scala-postgresql" % Versions.testcontainersVersion,
    "com.dimafeng" %% "testcontainers-scala-kafka"      % Versions.testcontainersVersion,
  )

}
