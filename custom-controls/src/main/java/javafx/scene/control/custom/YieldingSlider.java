package javafx.scene.control.custom;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class YieldingSlider extends Slider {

    public YieldingSlider() {
        addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent event) {
                lastTimeMousePressed = System.currentTimeMillis();
            }
        });
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
