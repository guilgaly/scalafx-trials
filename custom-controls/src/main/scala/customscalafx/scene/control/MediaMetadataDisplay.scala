package customscalafx.scene.control

import javafx.beans.property.MapProperty

import customjavafx.scene.{control => jfxsc}

import scala.language.implicitConversions
import scalafx.scene.layout.GridPane

object MediaMetadataDisplay {
  implicit def sfxDisplay2jfx(v: MediaMetadataDisplay) = v.delegate
}

class MediaMetadataDisplay(override val delegate: jfxsc.MediaMetadataDisplay = new jfxsc.MediaMetadataDisplay) extends GridPane {

  /** The metadata to display. */
  def metadata: MapProperty[String, AnyRef] = delegate.getMetadata
}
