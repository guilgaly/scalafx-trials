package guilgaly.fxtest.mp3player

import org.log4s._

import javafx.beans.property.{SimpleDoubleProperty, DoubleProperty}

import scalafx.Includes._
import scalafx.beans.property.ReadOnlyObjectWrapper
import scalafx.scene.media.{Media, MediaPlayer}
import scalafx.util.Duration

/**
 * Plays audio files.
 */
class Mp3Player {
  private[this] val logger = getLogger

  private[this] var mediaPlayer: Option[MediaPlayer] = None

  /** Audio volume, ranging from 0.0 to 1.0. */
  val volume: DoubleProperty = new SimpleDoubleProperty(1.0)
  private[this] val _currentTime = ReadOnlyObjectWrapper(new Duration(0))
  /** Current media playback time. */
  val currentTime = _currentTime.readOnlyProperty
  private[this] val _media = ReadOnlyObjectWrapper[Media](null.asInstanceOf[Media])
  /** Media being played currently. */
  val media = _media.readOnlyProperty
  private[this] val _status = ReadOnlyObjectWrapper(MediaPlayer.Status.UNKNOWN)
  /** Status of the media player. */
  val status = _status.readOnlyProperty

  /**
   * Starts playing audio file referenced by the given URL.
   * @param url URL to audio file.
   */
  def playMedia(url: String): Unit = {
    logger.debug("playMedia: " + url)

    // Stop if already playing
    stop()

    // Start playing
    val media = new Media(url)
    mediaPlayer = Some(new MediaPlayer(media))
    mediaPlayer.foreach(mp => {
      mp.onReady = {
        // mp.setVolume(volume.get())
        mp.volume <==> volume
        _currentTime <== mp.currentTime
        _status <== mp.status
        _media.value = media
        play()
      }
      mp.onEndOfMedia = stop()
    })

  }

  /**
   * Start playing the current audio file. Does nothing if already playing or if there is no current media file.
   */
  def play(): Unit = {
    logger.debug("play")
    mediaPlayer.foreach(_.play())
  }

  /**
   * Pauses playing the current audio file. Does nothing if already paused or if there is no current media file.
   */
  def pause(): Unit = {
    logger.debug("pause")
    mediaPlayer.foreach(_.pause())
  }

  /**
   *
   */
  def stop(): Unit = {
    logger.debug("stop")
    mediaPlayer.foreach(_.stop())
  }

  def seek(seekTime: Duration): Unit = {
    logger.debug("seek: " + seekTime.toMillis + " ms")
    mediaPlayer.foreach(_.seek(seekTime))
  }
}
