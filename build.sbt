organization  := "com.practicingtechie"

name := "scala-with-cats-ex"

version := "0.0.1"

scalaVersion := "2.12.7"

fork := true

scalacOptions := Seq("-feature", "-encoding", "utf8",
  "-deprecation", "-unchecked", "-Xlint", "-Yrangepos", "-Ypartial-unification")


val scalaLoggingVersion = "3.9.0"
val logBackVersion = "1.2.3"


libraryDependencies ++= Seq(
  "com.typesafe.scala-logging" %% "scala-logging"   % scalaLoggingVersion,
  "ch.qos.logback"              % "logback-classic" % logBackVersion % "runtime",
  "org.typelevel"              %% "cats-core"       % "1.3.1"
)


addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)
