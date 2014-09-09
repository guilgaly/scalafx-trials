package guilgaly.fxtest.mp3player

import javafx.scene.control.SeparatorMenuItem

import customscalafx.scene.control.{MediaMetadataDisplay, YieldingSlider}
import org.log4s._

import javafx.scene.{media => jfxm}

import scala.util.control.NonFatal
import scalafx.Includes._
import scalafx.application.Platform
import scalafx.event.ActionEvent

import scalafx.scene.control._
import scalafx.scene.input._
import scalafx.scene.layout.Pane
import scalafx.stage.FileChooser
import scalafx.stage.FileChooser.ExtensionFilter
import scalafx.util.Duration
import scalafxml.core.macros.sfxml

/**
 * Controller for the main scene in the MP3 Player Application.
 *
 * @param mainScene The Main scene.
 * @param menuBar The menu bar.
 * @param metadataDisplay Table for displaying all metadata of currently playing audio file.
 * @param startStopButton Buton to start or pause playback.
 * @param seekSlider Progress bar showing where we are in the currently playing audio file; also allows seeking.
 * @param timeDisplay Displays current play time and duration of the audio file.
 * @param volumeSlider Slider used for adjusting audio volume.
 * @param mp3Player Plays audio files.
 */
@sfxml
class Mp3PlayerController(
    private val mainScene: Pane,
    private val menuBar: MenuBar,
    private val fileMenu: Menu,
    private val metadataDisplay: MediaMetadataDisplay,
    private val startStopButton: Button,
    private val seekSlider: YieldingSlider,
    private val timeDisplay: Label,
    private val volumeSlider: Slider,
    private val mp3Player: Mp3Player) {
  private[this] val logger = getLogger

  if (seekSlider == null) {
    sys.error("seekSlider is null - probably because the scalafxml version used doesn't include patch for customscalafx/customjavafx classes.")
  }

  // Config
  menuBar.useSystemMenuBar = true
  if (System.getProperty("os.name") != "Mac OS X") {
    // On Mac OS X, the application menu already contains a "Quit" item.
    val sep = new SeparatorMenuItem
    val quit = new MenuItem("Quit")
    quit.onAction = (e: ActionEvent) => onQuit(e)
    quit.accelerator = new KeyCodeCombination(KeyCode.Q, KeyCombination.ShortcutDown)

    fileMenu.items ++= Seq(sep, quit)
  }

  // Drag & drop
  mainScene.onDragOver = (e: DragEvent) => {
    val db = e.getDragboard
    if (db.hasFiles || db.hasUrl) e.acceptTransferModes(TransferMode.LINK)
    else e.consume()
  }
  mainScene.onDragDropped = (e: DragEvent) => {
    val db = e.getDragboard
    if (db.hasFiles) {
      if (db.getFiles.size > 0) {
        try {
          val filePath = db.getFiles.get(0).toURI.toURL.toString
          mp3Player.playMedia(filePath)
        } catch {
          case NonFatal(e) => logger.error(e)("Cannot open " + db.getFiles.get(0))
        }
      }
    } else {
      // audio file from some host or jar
      mp3Player.playMedia(db.getUrl)
    }
    e.dropCompleted = true
    e.consume()
  }

  // Actions

  /** Opens music file. */
  def onOpen(event: ActionEvent): Unit = {
    val fileChooser = new FileChooser
    fileChooser.title = "Open audio file"
    fileChooser.extensionFilters.addAll(
      new ExtensionFilter("Audio Files", Seq("*.wav", "*.mp3", "*.aac")),
      new ExtensionFilter("All Files", "*.*"))
    val selectedFile = fileChooser.showOpenDialog(Mp3PlayerApp.stage)
    if (selectedFile != null) {
      mp3Player.playMedia(selectedFile.toURI.toURL.toString)
    }
  }

  /** Closes currently openned music file. */
  def onStop(event: ActionEvent): Unit = mp3Player.stop()
  /** Quits. */
  def onQuit(event: ActionEvent): Unit = Platform.exit()
  /** Starts or stops playback. */
  def onStartStop(event: ActionEvent): Unit = {
    if (mp3Player.status.value == jfxm.MediaPlayer.Status.PLAYING) mp3Player.pause()
    else mp3Player.play()
  }

  // Single button used for Start/Pause: update when status changes
  mp3Player.status.onChange { (_, _, newStatus) => {
    startStopButton.text = if (newStatus == jfxm.MediaPlayer.Status.PLAYING) "Pause" else "Play"
  }}

  // Volume
  volumeSlider.value <==> mp3Player.volume

  // Display correct metadataDisplay and time for current audio file
  mp3Player.media.onChange { (_, _, newVal) =>
    if (newVal != null) {
      seekSlider.max = newVal.duration.value.toSeconds
      mp3Player.currentTime.onChange { (_, _, newTime) => {
        if (!seekSlider.isValueChanging) { seekSlider.value = newTime.toSeconds }
        timeDisplay.text = format(newTime)
      }}
      // seekslider is continuously updated: seek only if it's changing because of user input
      seekSlider.value.onChange { (_, _, _) => {
        if (seekSlider.isValueChanging || seekSlider.mouseWasPressedWithinLast(100l)) {
          mp3Player.seek(Duration(seekSlider.getValue * 1000))
        }
      }}
      metadataDisplay.metadata.setValue(newVal.metadata)

    } else {
      seekSlider.value = 0
      seekSlider.max = 0
      timeDisplay.text = "00:00"
      metadataDisplay.metadata.setValue(null)
    }
  }

  /** Formats a duration as mm:ss (eg. "03:12" for 192 seconds). */
  private def format(dur: Duration): String = {
    val millis = dur.toMillis
    val seconds = ((millis / 1000) % 60).toInt
    val minutes = (millis / (1000 * 60)).toInt
    return f"$minutes%02d:$seconds%02d"
  }
}
