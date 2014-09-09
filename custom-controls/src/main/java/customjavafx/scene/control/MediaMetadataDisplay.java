package customjavafx.scene.control;

import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

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

        metadata.addListener((changed, oldVal, newVal) -> updateMetadata(newVal));
    }

    private final MapProperty<String, Object> metadata = new SimpleMapProperty<>();
    @FXML private ImageView coverView;
    @FXML private Label trackName;
    @FXML private Label artist;
    @FXML private Label album;
    @FXML private Label genre;
    @FXML private Label trackNumber;

    private void updateMetadata(Map<String, Object> metadata) {
        final Object image = metadata.get("image");
        if (image != null && image instanceof Image) {
            coverView.setImage((Image) image);
        } else {
            coverView.setImage(null);
        }
        trackName.setText(format(metadata.get("title")));
        artist.setText(format(metadata.get("artist")));
        album.setText(format(metadata.get("album")));
        genre.setText(format(metadata.get("genre")));
        trackNumber.setText(formatTrackNbre(metadata.get("track number"), metadata.get("track count")));
    }

    private String format(final Object obj) {
        return (obj != null) ? obj.toString() : "";
    }

    private String formatTrackNbre(final Object trackNbre, final Object trackCnt) {
        final String trackNbreStr = String.valueOf(trackNbre);
        final String trackCntStr = String.valueOf(trackCnt);

        final String trackDisplay;
        if (trackNbre == null && trackCnt == null) {
            trackDisplay = "";
        } else if (trackNbre != null && trackCnt == null) {
            trackDisplay = trackNbreStr;
        } else if (trackNbre == null && trackCnt != null) {
            trackDisplay = "? / " + trackCntStr;
        } else {
            trackDisplay = trackNbreStr + " / " + trackCntStr;
        }
        return trackDisplay;
    }

    /** The metadata to display. */
    public MapProperty<String, Object> getMetadata() {
        return metadata;
    }
}
