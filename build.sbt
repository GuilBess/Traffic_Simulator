ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.14"

lazy val root = (project in file("."))
  .settings(
    name := "Traffic Simulator"
  )

libraryDependencies += "io.github.cibotech" %% "evilplot" % "0.9.0"
libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "3.0.0"
