package customjavafx.scene.control;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

/**
 * Same as the {@link customjavafx.scene.control.YieldingSlider}, but using FXML and a separate controller class. Not needed for this simple example,
 * but usefull for more complex ones. Based on the UnlockCustom sample in the Scene Builder 2.0 Samples.
 */
public class FxmlYieldingSlider extends Slider {

    public FxmlYieldingSlider() {
        controller = load();

        addEventFilter(MouseEvent.MOUSE_PRESSED, event -> lastTimeMousePressed = System.currentTimeMillis());
    }

    public FxmlYieldingSlider(final double min, final double max, final double value) {
        this();
        setMin(min);
        setMax(max);
        setValue(value);
    }

    private FxmlYieldingSliderController load() {
        final FXMLLoader loader = new FXMLLoader();
        // fx:root is this node.
        loader.setRoot(this);
        // The FXMLLoader should use the class loader that loaded this class (Keypad).
        loader.setClassLoader(this.getClass().getClassLoader());
        // Keypad.fxml contains the configuration for 'this'
        loader.setLocation(this.getClass().getResource("FxmlYieldingSlider.fxml"));

        try {
            final Object root = loader.load();
            assert root == this;
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }

        final FxmlYieldingSliderController keypadController = loader.getController();
        assert keypadController != null;
        return keypadController;
    }

    private final FxmlYieldingSliderController controller;
    private long lastTimeMousePressed = 0;

    /**
     * @param t milliseconds
     * @return true if mouse was pressed less than t milliseconds ago.
     */
    public boolean mouseWasPressedWithinLast(final long t) {
        return (System.currentTimeMillis() - lastTimeMousePressed) <= t;
    }
}
