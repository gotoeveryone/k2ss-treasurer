name := """k2ss-treasurer"""
organization := "info.k2ss"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.2"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.0.0" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "info.k2ss.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "info.k2ss.binders._"
