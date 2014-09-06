package guilgaly.fxtest.mp3player

import org.log4s._

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafxml.core.FXMLView
import scalafxml.subcut.SubCutDependencyResolver

object Mp3PlayerApp extends JFXApp{
  private val logger = getLogger
  implicit val bindingModule = new Mp3PlayerConfiguration

  val mp3Player = bindingModule.inject[Mp3Player](None)

  stage = new JFXApp.PrimaryStage {
    title = "MP3 Player"
    scene = new Scene(FXMLView(getClass.getResource("/Mp3Player.fxml"), new SubCutDependencyResolver))
    minWidth = 400
    minHeight = 200
  }
}
