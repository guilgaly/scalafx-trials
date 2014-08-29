package guilgaly.fxtest.calculator

import Operator._

import org.log4s._

import javafx.event.EventHandler
import javafx.scene.input.{KeyCode, KeyEvent}

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafxml.core.FXMLView
import scalafxml.subcut.SubCutDependencyResolver

object CalculatorApp extends JFXApp {
  private val logger = getLogger

  implicit val bindingModule = new CalculatorConfiguration

  val calculator = bindingModule.inject[Calculator](None)
  val digitPattern = "([0-9])".r

  stage = new JFXApp.PrimaryStage() {
    title = "Calculator"
    scene = new Scene(FXMLView(getClass.getResource("/calculator.fxml"), new SubCutDependencyResolver()))

    scene.value.addEventHandler(KeyEvent.KEY_TYPED, new EventHandler[KeyEvent] {
      def handle(event: KeyEvent): Unit = {
        logger.debug(s"Key typed char: ${event.getCharacter}")
        event.getCharacter match {
          // Operators
          case "+" => calculator.enterOperator(Plus)
          case "-" => calculator.enterOperator(Minus)
          case ("*"|"x"|"×") => calculator.enterOperator(Multiply)
          case ("/"|"÷") => calculator.enterOperator(Divide)
          // Actions
          case ("c"|"C") => calculator.enterClear()
          case "=" => calculator.enterEquals()
          // Separator
          case ("."|",") => calculator.enterSeparator()
          // Digits
          case digitPattern(d) => calculator.enterDigit(d.toInt)
          case _ => // Do nothing
        }
      }
    })
    scene.value.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler[KeyEvent] {
      def handle(event: KeyEvent): Unit = {
        logger.debug(s"Key released code: ${event.getCode}")
        if (event.getCode == KeyCode.ENTER) calculator.enterEquals()
      }
    })
  }
}
