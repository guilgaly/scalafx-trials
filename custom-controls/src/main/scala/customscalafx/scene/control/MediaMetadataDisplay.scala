package customscalafx.scene.control

import scala.language.implicitConversions

import javafx.scene.{media => jfxm}
import customjavafx.scene.{control => jfxsc}

import scalafx.Includes._
import scalafx.beans.property.ObjectProperty
import scalafx.scene.layout.GridPane

object MediaMetadataDisplay {
  implicit def sfxDisplay2jfx(v: MediaMetadataDisplay) = v.delegate
}

class MediaMetadataDisplay(override val delegate: jfxsc.MediaMetadataDisplay = new jfxsc.MediaMetadataDisplay) extends GridPane {

  def media: ObjectProperty[jfxm.Media] = delegate.mediaProperty
  def media_=(m: jfxm.Media) = {
    media() = m
  }
}
