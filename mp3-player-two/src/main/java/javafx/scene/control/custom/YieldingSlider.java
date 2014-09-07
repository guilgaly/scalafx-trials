package javafx.scene.control.custom;

import javafx.event.EventHandler;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

public class YieldingSlider extends Slider {

    public YieldingSlider(final double min, final double max, final double value) {
        super(min, max, value);
    }

    private long lastTimeMousePressed = 0;

    public YieldingSlider() {
        addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent event) {
                lastTimeMousePressed = System.currentTimeMillis();
            }
        });
    }

    public boolean mouseWasPressedWithinLast(final long t) {
        return (System.currentTimeMillis() - lastTimeMousePressed) <= t;
    }
}
