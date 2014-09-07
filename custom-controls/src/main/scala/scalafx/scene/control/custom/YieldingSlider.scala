package scalafx.scene.control.custom

import javafx.scene.{control => jfxsc}

import scalafx.scene.control.Slider

object YieldingSlider {
  implicit def sfxSlider2jfx(v: YieldingSlider) = v.delegate
}

class YieldingSlider(override val delegate: jfxsc.custom.YieldingSlider = new jfxsc.custom.YieldingSlider) extends Slider {

   /** Constructs a Slider control with the specified slider min, max and current value values. */
   def this(min:Double, max:Double, value:Double) {
     this(new jfxsc.custom.YieldingSlider(min, max, value))
   }
 }
