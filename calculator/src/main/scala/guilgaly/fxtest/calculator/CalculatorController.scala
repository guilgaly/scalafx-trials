package guilgaly.fxtest.calculator

import org.log4s._

import javafx.scene.control.{Button => JfxButton}

import scalafx.event.ActionEvent
import scalafx.scene.control.{Button, Label}
import scalafxml.core.macros.sfxml

@sfxml
class CalculatorController(private val displayResult: Label,
                           private val displayOperation: Label,
                           private val calculator: Calculator) {
  private[this] val logger = getLogger

  // Display
  displayOperation.text <== calculator.operation
  displayResult.text <== calculator.result

  // Buttons
  def onNumberPressed(event: ActionEvent):Unit = {
    logger.debug(btnDescr(event))
    val nbrBtn: JfxButton = event.getSource.asInstanceOf[JfxButton]
    val nbr = nbrBtn.getText.toInt
    calculator.enterDigit(nbr)
  }
  def onSeparatorPressed(event: ActionEvent):Unit = {
    logger.debug(btnDescr(event))
    calculator.enterSeparator()
  }
  def onPlusMinusPressed(event: ActionEvent):Unit = {
    logger.debug(btnDescr(event))
    calculator.enterPlusMinus()
  }
  def onOperatorPressed(event: ActionEvent):Unit = {
    logger.debug(btnDescr(event))
    val operBtn: JfxButton = event.getSource.asInstanceOf[JfxButton]
    val oper = operBtn.getId match {
      case "buttonOperDiv" => Divide
      case "buttonOperMult" => Multiply
      case "buttonOperMinus" => Minus
      case "buttonOperPlus" => Plus
    }
    calculator.enterOperator(oper)
  }
  def onEqualsPressed(event: ActionEvent):Unit = {
    logger.debug(btnDescr(event))
    calculator.enterEquals()
  }
  def onClearPressed(event: ActionEvent):Unit = {
    logger.debug(btnDescr(event))
    calculator.enterClear()
  }

  private def btnDescr(event: ActionEvent): String = s"Button pressed: ${event.getSource.asInstanceOf[JfxButton].getText}"
}
