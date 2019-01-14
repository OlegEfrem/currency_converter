import sbt.util
name := "currency_converter"

organization := "com.oef"

version := "0.1.0"

scalaVersion := "2.12.8"

scalacOptions ++=
  Seq(
    "-feature",
    "-deprecation",
    "-unchecked",
    "-language:reflectiveCalls",
    "-language:postfixOps",
    "-language:implicitConversions",
    "-target:jvm-1.8",
    "-encoding", "utf8",
    "-Xfatal-warnings"
  )

libraryDependencies ++= {
  val scalaTestVersion = "3.0.5"
  val scalaMockVersion = "3.6.0"
  val jacksonVersion = "2.9.8"
  Seq(
    "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310" % jacksonVersion,
    "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonVersion,    
    "org.scalatest" %% "scalatest" % scalaTestVersion % Test,
    "org.scalamock" %% "scalamock-scalatest-support" % scalaMockVersion % Test,
  )
}

resolvers += Resolver.sonatypeRepo("public")

enablePlugins(JavaAppPackaging)

// run scalaStyle on compile:
lazy val compileScalastyle = taskKey[Unit]("compileScalastyle")

compileScalastyle := scalastyle.in(Compile).toTask("").value

(compile in Compile) := ((compile in Compile) dependsOn compileScalastyle).value

// run scalaStyle on test sources:
lazy val testScalastyle = taskKey[Unit]("testScalastyle")

testScalastyle := scalastyle.in(Test).toTask("").value

(test in Test) := ((test in Test) dependsOn testScalastyle).value

// code coverage configuration
coverageEnabled := true

coverageHighlighting := true

coverageMinimum := 100

coverageFailOnMinimum := true

// sbt-updates configurations: see => https://github.com/rtimush/sbt-updates
dependencyUpdatesFailBuild := true

Revolver.settings
