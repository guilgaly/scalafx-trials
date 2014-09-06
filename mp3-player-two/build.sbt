net.virtualvoid.sbt.graph.Plugin.graphSettings // for dependency-tree, etc.

packageArchetype.java_application // for packaging

jfxSettings

JFX.mainClass := Some("guilgaly.fxtest.mp3player.Mp3PlayerApp")

JFX.nativeBundles := "all"

JFX.vendor := "Guillaume Galy"

JFX.title := "MP3 Player"

JFX.description := "Plays audio files"

JFX.copyright := "Guillaume Galy 2014"

JFX.license := "MIT"