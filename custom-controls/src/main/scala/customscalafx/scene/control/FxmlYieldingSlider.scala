package customscalafx.scene.control

import scala.language.implicitConversions
import customjavafx.scene.{control => jfxsc}
import scalafx.scene.control.Slider

object FxmlYieldingSlider {
  implicit def sfxSlider2jfx(v: FxmlYieldingSlider) = v.delegate
}

class FxmlYieldingSlider(override val delegate: jfxsc.FxmlYieldingSlider = new jfxsc.FxmlYieldingSlider) extends Slider {

  /** Constructs a Slider control with the specified slider min, max and current value values. */
  def this(min: Double, max: Double, value: Double) {
    this(new jfxsc.FxmlYieldingSlider(min, max, value))
  }
}
