package guilgaly.fxtest.mp3player

import org.log4s._

import javafx.scene.media.MediaPlayer.Status

import scala.util.control.NonFatal
import scalafx.Includes._
import scalafx.application.Platform
import scalafx.beans.property.StringProperty
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.scene.control.TableColumn.CellDataFeatures

import scalafx.scene.control._
import scalafx.scene.control.custom.YieldingSlider
import scalafx.scene.input.{TransferMode, DragEvent, MouseEvent}
import scalafx.scene.media.{Media, MediaPlayer}
import scalafx.stage.FileChooser
import scalafx.stage.FileChooser.ExtensionFilter
import scalafx.util.Duration
import scalafxml.core.macros.sfxml

@sfxml
class Mp3PlayerController(
    private val menuBar: MenuBar,
    private val metadataTable: TableView[(String, AnyRef)],
    private val startStopButton: Button,
    private val seekSlider: YieldingSlider,
    private val timeDisplay: Label,
    private val volumeSlider: Slider,
    private val mp3Player: Mp3Player) {
  private[this] val logger = getLogger

  logger.debug(seekSlider.toString())

  // Config

  menuBar.useSystemMenuBar = true

//  seekSlider.addEventFilter[javafx.scene.input.MouseEvent](MouseEvent.MousePressed, (event: MouseEvent) => { lastTimeMousePressed = System.currentTimeMillis() })
//  private var lastTimeMousePressed = 0l
//  /**
//   * @return true, if user was pressed mouse button within last `t` ms
//   */
//  def mouseWasPressedWithinLast(t: Long): Boolean = (System.currentTimeMillis() - lastTimeMousePressed) <= t

  private val keyCol = new TableColumn[(String, AnyRef), String]("Metadata")
  keyCol.cellValueFactory = (p: CellDataFeatures[(String, AnyRef), String]) => new StringProperty(p.value._1)
  private val valCol = new TableColumn[(String, AnyRef), String]("Value")
  valCol.cellValueFactory = (p: CellDataFeatures[(String, AnyRef), String]) => new StringProperty(p.value._2.toString)
  metadataTable.columns.clear()
  metadataTable.columns ++= Seq(keyCol, valCol)

  // Drag & drop

  metadataTable.onDragOver = (e: DragEvent) => {
    val db = e.getDragboard
    if (db.hasFiles || db.hasUrl) e.acceptTransferModes(TransferMode.LINK)
    else e.consume()
  }
  metadataTable.onDragDropped = (e: DragEvent) => {
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
  def onClose(event: ActionEvent): Unit = mp3Player.stop()
  /** Quits. */
  def onQuit(event: ActionEvent): Unit = Platform.exit()
  /** Starts or stops playback. */
  def onStartStop(event: ActionEvent): Unit = {
    if (mp3Player.status.value == Status.PLAYING) mp3Player.pause()
    else mp3Player.play()
  }

  mp3Player.status.onChange { (_, _, newStatus) => {

    startStopButton.text = if (newStatus == Status.PLAYING) "Pause" else "Play"
  }}

  volumeSlider.value <==> mp3Player.volume

  mp3Player.media.onChange { (_, _, newVal) =>
    if (newVal != null) {
      seekSlider.max = newVal.duration.value.toSeconds
      mp3Player.currentTime.onChange { (_, _, newTime) => {
        if (!seekSlider.isValueChanging) { seekSlider.value = newTime.toSeconds }
        timeDisplay.text = format(newTime)
      }}
      seekSlider.value.onChange { (_, _, _) => {
        if (seekSlider.isValueChanging || seekSlider.mouseWasPressedWithinLast(100l)) {
          mp3Player.seek(Duration(seekSlider.getValue * 1000))
        }
      }}
      metadataTable.items = ObservableBuffer(newVal.metadata.toList)
      newVal.metadata.onChange { (newMap, _) => {
        metadataTable.items = ObservableBuffer(newMap.toList)
      }}
    } else {
      seekSlider.value = 0
      seekSlider.max = 0
      timeDisplay.text = "00:00"
      metadataTable.items = ObservableBuffer()
    }
  }

  private def format(dur: Duration): String = {
    val millis = dur.toMillis
    val seconds = ((millis / 1000) % 60).toInt
    val minutes = (millis / (1000 * 60)).toInt
    return f"$minutes%02d:$seconds%02d"
  }
}
