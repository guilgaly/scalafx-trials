package customscalafx.scene.control

import scala.language.implicitConversions
import customjavafx.scene.{control => jfxsc}
import scalafx.scene.control.Slider

object YieldingSlider {
  implicit def sfxSlider2jfx(v: YieldingSlider) = v.delegate
}

class YieldingSlider(override val delegate: jfxsc.YieldingSlider = new jfxsc.YieldingSlider) extends Slider {

  /** Constructs a Slider control with the specified slider min, max and current value values. */
  def this(min: Double, max: Double, value: Double) {
    this(new jfxsc.YieldingSlider(min, max, value))
  }
}
