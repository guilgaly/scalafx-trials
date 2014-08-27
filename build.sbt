name := "scalafx-trials"

def scalafxProject(name: String): Project = (
  Project(name, file(name))
  settings (
    organization := "guilgaly",
    scalaVersion := "2.11.2",
    libraryDependencies ++= Seq(
      // ScalaFX
      "org.scalafx" %% "scalafx" % "8.0.5-R5",
      "org.scalafx" %% "scalafxml-core" % "0.2.1-SNAPSHOT",
      "org.scalafx" %% "scalafxml-subcut" % "0.2.1-SNAPSHOT",
      // IO
      "com.github.scala-incubator.io" %% "scala-io-core" % "0.4.3",
      "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.3",
      // DI/IoC
      "com.escalatesoft.subcut" %% "subcut" % "2.1",
      // Database
      "com.typesafe.slick" %% "slick" % "2.1.0",
      "com.h2database" % "h2" % "1.4.181",
      // Logs
      "org.log4s" %% "log4s" % "1.0.3",
      "org.slf4j" % "slf4j-simple" % "1.7.7",
      // Tests
      "org.scalatest" %% "scalatest" % "2.2.1" % "test",
      // Compiler plugins
      compilerPlugin("org.scalamacros" % "paradise" % "2.0.1" cross CrossVersion.full),
      compilerPlugin("com.escalatesoft.subcut" %% "subcut" % "2.1")),
    resolvers += Opts.resolver.sonatypeSnapshots,
    // Set the prompt (for this build) to include the project id.
    shellPrompt := { state => System.getProperty("user.name") + ":" + Project.extract(state).currentRef.project + "> " },
    // Run in separate VM, so there are no issues with double initialization of JavaFX
    fork := true,
    fork in Test := true
  )
)

val calculator = (
  scalafxProject("calculator")
  settings (
    version := "0.0.1-SNAPSHOT"
  )
)

net.virtualvoid.sbt.graph.Plugin.graphSettings // for dependency-tree, etc.