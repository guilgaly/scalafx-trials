package guilgaly.fxtest.mp3player

import org.log4s._

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafxml.core.FXMLView
import scalafxml.subcut.SubCutDependencyResolver

/**
 * Main object for launching the MP3 Player application.
 */
object Mp3PlayerApp extends JFXApp {

  /** Subcut configuration. */
  implicit val bindingModule = new Mp3PlayerConfiguration

  stage = new JFXApp.PrimaryStage {
    title = "MP3 Player"
    scene = new Scene(FXMLView.apply(getClass.getResource("/Mp3Player.fxml"), new SubCutDependencyResolver))
    minWidth = 400
    minHeight = 200
  }
}
