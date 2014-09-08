package customjavafx.scene.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 *
 */
public class MediaMetadataDisplay extends GridPane {

    public MediaMetadataDisplay() {
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MediaMetadataDisplay.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        media.addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                updateMedia(newVal);
                newVal.getMetadata().addListener((MapChangeListener<String, Object>) change -> updateMedia(newVal));
            } else {
                resetDisplay();
            }
        });
    }

    private final Logger logger = LoggerFactory.getLogger(MediaMetadataDisplay.class);
    private final ObjectProperty<Media> media = new SimpleObjectProperty<>((Media) null, "media");
    @FXML private ImageView coverView;
    @FXML private Label trackName;
    @FXML private Label artist;
    @FXML private Label album;
    @FXML private Label genre;
    @FXML private Label trackNumber;

    public void updateMedia(Media media) {
        display(media);
    }

    private void display(final Media media) {
        final Map<String, Object> metadata = media.getMetadata();

        logger.debug("display");
        for (Map.Entry<String, Object> entry : metadata.entrySet()) {
            logger.debug(entry.getKey() + ": " + entry.getValue());
        }

        final Object image = metadata.get("image");
        if (image != null && image instanceof Image) {
            coverView.setImage((Image) image);
        }

        trackName.setText(format(metadata.get("title")));
        artist.setText(format(metadata.get("artist")));
        album.setText(format(metadata.get("album")));
        genre.setText(format(metadata.get("genre")));

        final String trackNbre = format(metadata.get("track number"));
        final String trackCnt = format(metadata.get("track count"));
        final String trackDisp = trackNbre + ((trackCnt != null) ? ("/" + trackCnt) : null );
        trackNumber.setText(trackDisp);
    }

    private void resetDisplay() {
        coverView.setImage(null);
        trackName.setText(null);
        artist.setText(null);
        artist.setText(null);
        album.setText(null);
        genre.setText(null);
        trackNumber.setText(null);
    }

    private String format(final Object value) {
        final String fmt;
        if (value == null) {
            fmt = null;
        } else {
            fmt = value.toString();
        }
        return fmt;
    }

    public ObjectProperty<Media> mediaProperty() {
        return media;
    }
    public Media getMedia() {
        return mediaProperty().get();
    }
    public void setMedia(final Media media) {
        mediaProperty().set(media);
    }
}
