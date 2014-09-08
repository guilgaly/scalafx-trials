package customjavafx.scene.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;

import java.io.IOException;

public class MediaMetadataDisplay extends GridPane {

    public MediaMetadataDisplay() {
        controller = load();
        media = new SimpleObjectProperty<>((Media) null, "media");

        media.addListener((obs, oldVal, newVal) -> {
            controller.setMedia(newVal);
        });
    }

    private final MediaMetadataDisplayController controller;
    private final ObjectProperty<Media> media;

    private MediaMetadataDisplayController load() {
        final FXMLLoader loader = new FXMLLoader();
        loader.setRoot(this);
        loader.setClassLoader(this.getClass().getClassLoader());
        loader.setLocation(this.getClass().getResource("MediaMetadataDisplay.fxml"));
        try {
            final Object root = loader.load();
            assert root == this;
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
        final MediaMetadataDisplayController controller = loader.getController();
        assert controller != null;
        return controller;
    }

    public ObjectProperty<Media> mediaProperty() {
        return media;
    }
    public Media getMedia() {
        return media.get();
    }
    public void setMedia(final Media media) {
        this.media.set(media);
    }
}
