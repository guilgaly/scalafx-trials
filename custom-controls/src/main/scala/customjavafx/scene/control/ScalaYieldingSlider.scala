package customjavafx.scene.control

import javafx.event.EventHandler
import javafx.scene.control.Slider
import javafx.scene.input.MouseEvent

/**
 * Same as [[customjavafx.scene.control.YieldingSlider]], but written in Scala instead of Java.
 */
class ScalaYieldingSlider extends Slider{
  def this(min: Double, max: Double, value: Double) = {
    this()
    setMin(min)
    setMax(max)
    setValue(value)
  }

  // Support for Java 8 SAMs (lambdas) is still experimental in Scala 2.11.
  // I used the old-school anonymous class instead.
  addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler[MouseEvent] {
    def handle(event: MouseEvent): Unit = lastTimeMousePressed = System.currentTimeMillis
  })

  private var lastTimeMousePressed: Long = 0

  /**
   * @param t milliseconds
   * @return true if mouse was pressed less than t milliseconds ago.
   */
  def mouseWasPressedWithinLast(t: Long): Boolean =
    (System.currentTimeMillis - lastTimeMousePressed) <= t
}
