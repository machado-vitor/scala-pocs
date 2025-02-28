ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.6.3"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.5.2",
  "org.slf4j" % "slf4j-nop" % "2.0.16",
  "org.postgresql" % "postgresql" % "42.7.5",
)

lazy val root = (project in file("."))
  .settings(
    name := "slick-poc"
  )




