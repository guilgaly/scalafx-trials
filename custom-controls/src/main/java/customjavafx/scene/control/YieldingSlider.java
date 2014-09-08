package customjavafx.scene.control;

import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

/**
 * A {@link javafx.scene.control.Slider} which also allows to know if the mouse was pressed recently.
 */
public class YieldingSlider extends Slider {

    public YieldingSlider() {
        addEventFilter(MouseEvent.MOUSE_PRESSED, event -> lastTimeMousePressed = System.currentTimeMillis());
    }

    public YieldingSlider(final double min, final double max, final double value) {
        this();
        setMin(min);
        setMax(max);
        setValue(value);
    }

    private long lastTimeMousePressed = 0;

    /**
     * @param t milliseconds
     * @return true if mouse was pressed less than t milliseconds ago.
     */
    public boolean mouseWasPressedWithinLast(final long t) {
        return (System.currentTimeMillis() - lastTimeMousePressed) <= t;
    }
}
