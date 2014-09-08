package customscalafx.scene.control

import scala.language.implicitConversions
import customjavafx.scene.{control => jfxsc}
import scalafx.scene.control.Slider

object ScalaYieldingSlider {
  implicit def sfxSlider2jfx(v: ScalaYieldingSlider) = v.delegate
}

class ScalaYieldingSlider(override val delegate: jfxsc.ScalaYieldingSlider = new jfxsc.ScalaYieldingSlider) extends Slider {

  /** Constructs a Slider control with the specified slider min, max and current value values. */
  def this(min: Double, max: Double, value: Double) {
    this(new jfxsc.ScalaYieldingSlider(min, max, value))
  }
}