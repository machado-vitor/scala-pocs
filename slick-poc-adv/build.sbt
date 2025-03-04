ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.6.3"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.5.2",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.5.2",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
  "ch.qos.logback" % "logback-classic" % "1.5.17",
  "org.postgresql" % "postgresql" % "42.7.5",
)


lazy val root = (project in file("."))
  .settings(
    name := "slick-poc-adv"
  )
