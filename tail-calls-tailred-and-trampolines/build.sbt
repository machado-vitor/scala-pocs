ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.6.3"

lazy val root = (project in file("."))
  .settings(
    name := "tail-calls-tailred-and-trampolines",
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-free" % "2.13.0",
      "org.typelevel" %% "cats-effect" % "3.5.7"
    )
  )
