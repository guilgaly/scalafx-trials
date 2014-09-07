package guilgaly.fxtest.mp3player

import scalafx.Includes._
import scalafx.scene.control.custom.YieldingSlider
import scalafxml.core.macros.sfxml

@sfxml
class SomeController(private val seekSlider: YieldingSlider) {
  seekSlider.value.onChange { (_, _, _) => {
    if (seekSlider.mouseWasPressedWithinLast(100l)) {
      println("seekSlider.mouseWasPressedWithinLast(100l)")
    }
  }}
}
