ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.7.3"

lazy val SpringBootVersion = "3.5.6"

lazy val root = (project in file("."))
  .settings(
    name := "spring-global-error-handler",
    libraryDependencies ++= Seq(
      "org.springframework.boot" % "spring-boot-starter-web" % SpringBootVersion,
      "org.springframework.boot" % "spring-boot-starter-validation" % SpringBootVersion,
      "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.20.0",
      "org.scalatest" %% "scalatest" % "3.2.19" % Test,
      "org.springframework.boot" % "spring-boot-starter-test" % SpringBootVersion % Test
    )
  )
