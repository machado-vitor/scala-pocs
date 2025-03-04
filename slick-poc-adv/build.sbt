name := "slick-codegen"
version := "1.0"

scalaVersion := "3.5.2"

scalacOptions += "-deprecation"

val slickVersion = "3.5.2"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick"         % slickVersion,
  "com.typesafe.slick" %% "slick-codegen" % slickVersion,
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
  "org.postgresql"      % "postgresql"    % "42.7.5",
  "org.slf4j"           % "slf4j-api"      % "2.0.17",
  "ch.qos.logback"      % "logback-classic" % "1.5.17"
)

lazy val slick = taskKey[Seq[File]]("Generate Tables.scala")

(Compile / sourceGenerators) += slick.taskValue

slick := {
  val log = streams.value.log

  val outputDir = (Compile / sourceManaged).value / "slick"

  val url         = "jdbc:postgresql://localhost:5433/slick_user_address_db"
  val user        = "user"
  val password    = "password"
  val jdbcDriver  = "org.postgresql.Driver"
  val slickDriver = "slick.jdbc.PostgresProfile"

  val pkg = "demo"

  log.info(s"Running Slick codegen. Output folder: $outputDir, package: $pkg")

  // Build classpath for codegen
  val cp = (Compile / dependencyClasspath).value

  // Invoke Slick code generator
  runner.value.run(
    mainClass = "slick.codegen.SourceCodeGenerator",
    classpath = cp.files,
    options = Array(
      slickDriver,
      jdbcDriver,
      url,
      outputDir.getPath,
      pkg,
      user,
      password
    ),
    log = log
  ).failed.foreach(err => sys.error(s"Slick codegen failed: ${err.getMessage}"))

  val file = outputDir / pkg / "Tables.scala"
  log.info(s"Slick codegen complete. Generated file: $file")

  Seq(file)
}
