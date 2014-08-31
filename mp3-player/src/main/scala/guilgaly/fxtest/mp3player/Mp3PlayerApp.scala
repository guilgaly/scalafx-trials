package guilgaly.fxtest.mp3player

import javafx.beans.value.{ObservableValue, ChangeListener}
import javafx.scene.media.AudioSpectrumListener

import scala.util.Random
import scala.util.control.NonFatal
import scalafx.Includes._
import scalafx.application.{Platform, JFXApp}
import scalafx.application.JFXApp.PrimaryStage
import scalafx.beans.property.ReadOnlyDoubleProperty.sfxReadOnlyDoubleProperty2jfx
import scalafx.geometry.Point2D
import scalafx.scene.control.Slider
import scalafx.scene.media.{Media, MediaPlayer}
import scalafx.scene.media.MediaPlayer.Status
import scalafx.scene.shape._
import scalafx.scene.text.Text
import scalafx.scene.{Group, Node, Scene}
import scalafx.scene.input.{TransferMode, DragEvent, MouseEvent}
import scalafx.scene.paint.Color
import scalafx.stage.{StageStyle, WindowEvent}
import scalafx.util.Duration

object Mp3PlayerApp extends JFXApp {

  private var mediaPlayer: MediaPlayer = null
  private var anchorPt: Point2D = null
  private var previousLocation: Point2D = null
  private var progressListener: ChangeListener[javafx.util.Duration] = null

  private final val StopButtonId = "stop-button"
  private final val PlayButtonId = "play-button"
  private final val PauseButtonId = "pause-button"
  private final val CloseButtonId = "close-button"
  private final val VizContainerId = "viz-container"
  private final val SeekPosSliderId = "seek-position-slider"

  stage = new PrimaryStage {
    initStyle(StageStyle.TRANSPARENT)
    width = 551
    height = 270
    centerOnScreen()
    scene = new Scene {
      fill = Color.rgb(0, 0, 0, 1.0)
      stylesheets.add(getClass.getResource("/mp3-player.css").toExternalForm)
      content = Seq(
        applicationArea(this),
        vizContainer,
        buttonPanel(this),
        progressSlider(this),
        closeButton(this)
      )
    }
  }

  // Initialize stage to be movable via mouse
  initMovablePlayer()

  // Initializing to accept files
  // dragged over surface to load media
  initFileDragNDrop()



  /**
   * A simple rectangular area as the surface of the app.
   * @return Node a Rectangle node.
   */
  private def applicationArea(scene: Scene): Node = {
    val applicationArea = new Rectangle
    // add selector to style app-area
    applicationArea.id = "app-area"
    // make the app area rectangle the size of the scene.
    applicationArea.width <== scene.width
    applicationArea.height <== scene.height

    applicationArea
  }

  /**
   * @return Container for random circles bouncing about
   */
  private def vizContainer: Node = {
    val vizContainer = new Group()
    vizContainer.id = VizContainerId
    vizContainer
  }

  /**
   * Creates a node containing the audio player's
   *  stop, pause and play buttons.
   *
   * @return Node A button panel having play,
   *  pause and stop buttons.
   */
  private def buttonPanel(scene: Scene): Node = {
    // create button control panel
    val buttonGroup = new Group

    // Button area
    val buttonArea = Rectangle(60, 30)
    buttonArea.id = "button-area"
    buttonGroup.children.add(buttonArea)

    // stop button control
    val stopButton = Rectangle(10, 10)
    stopButton.id = StopButtonId
    stopButton.onMousePressed = (event: MouseEvent) =>
      if (mediaPlayer != null) {
        updatePlayAndPauseButtons(playVisible = true)
        if (mediaPlayer.getStatus == Status.PLAYING.delegate) mediaPlayer.stop()
      }

    // play button
    val playButton = Arc(
      12, // center x
      16, // center y
      15, // radius x
      15, // radius y
      150, // start angle
      60) // length
    playButton.id = PlayButtonId
    playButton.`type` = ArcType.ROUND
    playButton.onMousePressed = (event: MouseEvent) =>
      if (mediaPlayer != null) {
        updatePlayAndPauseButtons(playVisible = false)
        mediaPlayer.play()
      }

    // pause control
    val pauseButton = new Group
    pauseButton.id = PauseButtonId
    val pauseBackground = Circle(12, 16, 10)
    pauseBackground.styleClass.add("pause-circle")
    val firstLine = Line(
      6, // start x
      6, // start y
      6, // end x
      14); // end y
    firstLine.styleClass.add("pause-line")
    firstLine.style = "-fx-translate-x: 34;"
    val secondLine = Line(
      6, // start x
      6, // start y
      6, // end x
      14); // end y
    secondLine.styleClass.add("pause-line")
    secondLine.style = "-fx-translate-x: 38;"
    pauseButton.content.addAll(pauseBackground, firstLine, secondLine)
    pauseButton.onMousePressed = (event: MouseEvent) =>
      if (mediaPlayer != null) {
        updatePlayAndPauseButtons(playVisible = true)
        if (mediaPlayer.getStatus == Status.PLAYING.delegate) mediaPlayer.pause()
      }

    buttonGroup.content.addAll(stopButton, playButton, pauseButton)

    // move button group when scene is resized
    buttonGroup.translateX <== scene.width.subtract(buttonArea.width + 6)
    buttonGroup.translateY <== scene.height.subtract(buttonArea.height + 6)

    buttonGroup
  }

  /**
   * Sets play button visible and pause button not visible when
   * playVisible is true otherwise the opposite.
   *
   * @param playVisible - value of true the play becomes visible
   * and pause non visible, otherwise the opposite.
   */
  private def updatePlayAndPauseButtons(playVisible: Boolean) {
    val scene = stage.getScene
    val playButton = scene.lookup("#" + PlayButtonId)
    val pauseButton = scene.lookup("#" + PauseButtonId)

    // hide or show buttons
    playButton.setVisible(playVisible)
    pauseButton.setVisible(!playVisible)
    if (playVisible) {
      // show play button
      playButton.toFront()
      pauseButton.toBack()
    } else {
      // show pause button
      pauseButton.toFront()
      playButton.toBack()
    }
  }

  /**
   * A position slider to seek backward and forward
   * that is bound to a media player control.
   *
   * @return Slider control bound to media player.
   */
  private def progressSlider(scene: Scene): Node = {
    val slider = new Slider(0, 100, 1)
    slider.id = SeekPosSliderId
    slider.value.addListener((o: javafx.beans.value.ObservableValue[_ <: Number], oldVal: Number, newVal: Number) =>
      if (slider.isValueChanging) {
        // must check if media is paused before seeking
        if (mediaPlayer != null && mediaPlayer.getStatus == Status.PAUSED.delegate) {
          // convert seconds to millis
          val dur = slider.getValue * 1000
          mediaPlayer.seek(Duration(dur))
        }
      }
    )
    slider.translateX = 10
    slider.translateY <== scene.height.subtract(50)

    // update slider as video is progressing (later removal)
    progressListener = (observable: ObservableValue[_ <: javafx.util.Duration], oldValue: javafx.util.Duration, newValue: javafx.util.Duration) => slider.value = newValue.toSeconds

    slider
  }

  /**
   * The close button to exit application
   *
   * @return Node representing a close button.
   */
  private def closeButton(scene: Scene): Node = {
    val closeButton = new Group()
    closeButton.id = CloseButtonId
    val closeBackground = Circle(5, 0, 7)
    closeBackground.id = "close-circle"
    val closeXmark = new Text(2, 4, "X")
    closeButton.translateX <== scene.width.subtract(15)
    closeButton.translateY = 10
    closeButton.content.addAll(closeBackground, closeXmark)

    // exit app
    closeButton.onMouseClicked = (event: MouseEvent) => Platform.exit()

    closeButton
  }

  /**
   * Initialize the stage to allow the mouse cursor to move the application
   * using dragging.
   */
  private def initMovablePlayer(): Unit = {
    val scene = stage.getScene

    scene.onMousePressed = (event: MouseEvent) => anchorPt = new Point2D(event.screenX, event.screenY)

    scene.onMouseDragged = (event: MouseEvent) =>
      if (anchorPt != null && previousLocation != null) {
        stage.x = previousLocation.x + event.screenX - anchorPt.x
        stage.y = previousLocation.y + event.screenY - anchorPt.y
      }

    scene.onMouseReleased = (event: MouseEvent) => previousLocation = new Point2D(stage.getX, stage.getY)

    stage.onShown = (event: WindowEvent) => previousLocation = new Point2D(stage.getX, stage.getY)
  }

  /**
   * Initialize the Drag and Drop ability for media files.
   */
  private def initFileDragNDrop(): Unit = {
    val scene = stage.getScene

    scene.onDragOver = (event: DragEvent) => {
      val db = event.getDragboard
      if (db.hasFiles || db.hasUrl) event.acceptTransferModes(TransferMode.LINK)
      else event.consume()
    }

    // Dropping over surface
    scene.onDragDropped = (event: DragEvent) => {
      val db = event.getDragboard
      var success = false
      var filePath: String = null
      if (db.hasFiles) {
        success = true
        if (db.getFiles.size() > 0) {
          try {
            filePath = db.getFiles.get(0).toURI.toURL.toString
            playMedia(filePath)
          } catch {
            case NonFatal(e) => e.printStackTrace()
          }
        }
      } else {
        // audio file from some host or jar
        playMedia(db.getUrl)
        success = true
      }
      event.dropCompleted = success
      event.consume()
    }
  }

  /**
   * After a file is dragged onto the application a new MediaPlayer
   * instance is created with a media file.
   *
   * @param url The URL pointing to an audio file
   */
  private def playMedia(url: String): Unit = {
    val scene = stage.getScene

    if (mediaPlayer != null) {
      mediaPlayer.pause()
      val nullHandler = null.asInstanceOf[Unit]
      mediaPlayer.onPaused = nullHandler
      mediaPlayer.onPlaying = nullHandler
      mediaPlayer.onReady = nullHandler
      mediaPlayer.currentTime.removeListener(progressListener)
      mediaPlayer.setAudioSpectrumListener(null)
    }

    val media = new Media(url)
    mediaPlayer = new MediaPlayer(media)
    mediaPlayer.currentTime.addListener(progressListener)

    mediaPlayer.onReady = {
      media.metadata.foreach{case (name, value) => println(name + ": " + value)}
      updatePlayAndPauseButtons(playVisible = false)
      val progressSlider = scene.lookup("#" + SeekPosSliderId).asInstanceOf[javafx.scene.control.Slider]
      progressSlider.value = 0
      progressSlider.max = mediaPlayer.media.getDuration.toSeconds
      mediaPlayer.play()
    }

    mediaPlayer.onEndOfMedia = {
      updatePlayAndPauseButtons(playVisible = true)
      // change buttons to play and rewind
      mediaPlayer.stop()
    }

    val vizContainer = scene.lookup("#" + VizContainerId).asInstanceOf[javafx.scene.Group]
    mediaPlayer.audioSpectrumListener = new AudioSpectrumListener {
      def spectrumDataUpdate(timestamp: Double, duration: Double, magnitudes: Array[Float], phases: Array[Float]): Unit = {
        vizContainer.content.clear()
        var i = 0
        val x = 10
        val y = stage.getScene.height / 2
        val rand = new Random()
        for (phase <- phases) {
          val red = rand.nextInt(255)
          val green = rand.nextInt(255)
          val blue = rand.nextInt(255)
          val circle = Circle(10)
          circle.centerX = x + i
          circle.centerY = (y + (phase * 100)).toDouble
          circle.fill = Color.rgb(red, green, blue)
          vizContainer.content.add(circle)
          i += 5
        }
      }
    }
  }
}
