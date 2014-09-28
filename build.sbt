import sbt.Keys._

name := "scalafx-trials"

def scalafxProject(name: String): Project = (
  Project(name, file(name))
  settings(
    version := "0.0.1",
    organization := "guilgaly",
    scalaVersion := "2.11.2",
    libraryDependencies ++= Seq(
      // ScalaFX
      "org.scalafx" %% "scalafx" % "8.0.5-R5",
      // Logs
      "org.log4s" %% "log4s" % "1.0.3",
      "org.slf4j" % "slf4j-simple" % "1.7.7",
      // Tests
      "org.scalatest" %% "scalatest" % "2.2.1" % "test"
    ),
      // compilerPlugin("com.escalatesoft.subcut" %% "subcut" % "2.1")),
    resolvers += Opts.resolver.sonatypeSnapshots,
    // Set the prompt (for this build) to include the project id.
    shellPrompt := { state => System.getProperty("user.name") + ":" + Project.extract(state).currentRef.project + "> " },
    // Run in separate VM, so there are no issues with double initialization of JavaFX
    fork := true,
    fork in Test := true
  )
)

def scalafxmlProject(name: String): Project = (
  scalafxProject(name)
  settings(
    libraryDependencies ++= Seq(
      "org.scalafx" %% "scalafxml-core" % "0.2.2-SNAPSHOT",
      "org.scalafx" %% "scalafxml-subcut" % "0.2.2-SNAPSHOT",
      // DI/IoC
      "com.escalatesoft.subcut" %% "subcut" % "2.1",
      // Compiler plugins
      compilerPlugin("org.scalamacros" % "paradise" % "2.0.1" cross CrossVersion.full)
    )
  )
)

val calculator = scalafxmlProject("calculator")

val exempleCli = Project("exemple-cli", file("exemple-cli"))

val mp3Player = scalafxProject("mp3-player")

val customControls = scalafxProject("custom-controls")

val mp3PlayerTwo = (
  scalafxmlProject("mp3-player-two")
  dependsOn customControls
)