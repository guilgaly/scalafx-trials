package customjavafx.scene.control;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;

import java.util.Map;

public class MediaMetadataDisplayController {
    @FXML private ImageView coverView;
    @FXML private Label trackName;
    @FXML private Label artist;
    @FXML private Label album;
    @FXML private Label genre;
    @FXML private Label trackNumber;

    private Media media = null;

    public void setMedia(Media media) {
        this.media = media;
        if (media != null) {
            display(media);
        } else {
            resetDisplay();
        }
    }

    private void display(final Media media) {
        final Map<String, Object> metadata = media.getMetadata();

        final Object image = metadata.get("image");
        if (image != null && image instanceof Image) {
            coverView.setImage((Image) image);
        }

        trackName.setText((String) metadata.get("title"));
        artist.setText((String) metadata.get("artist"));
        album.setText((String) metadata.get("album"));
        genre.setText((String) metadata.get("genre"));

        final String trackNbre = (String) metadata.get("track number");
        final String trackCnt = (String) metadata.get("track count");
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
}
