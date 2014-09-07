package javafx.scene.control.custom;

import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

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

    public boolean mouseWasPressedWithinLast(final long t) {
        return (System.currentTimeMillis() - lastTimeMousePressed) <= t;
    }
}
