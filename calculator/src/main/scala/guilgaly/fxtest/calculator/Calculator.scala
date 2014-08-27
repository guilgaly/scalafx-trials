package guilgaly.fxtest.calculator

import org.log4s._

import scalafx.beans.property.{ReadOnlyStringProperty, ReadOnlyStringWrapper}

class Calculator {
  private[this] val logger = getLogger

  private var status: CalculatorStatus = Cleared
  private var lhs: Option[String] = None
  private var rhs: Option[String] = None
  private var oper: Option[Operator] = None

  private final val _operation = new ReadOnlyStringWrapper
  private final val _result = new ReadOnlyStringWrapper

  // calculator manipulations
  def enterDigit(digit: Int): Unit = {
    assert(digit >= 0 && digit <= 9)
    logger.debug(s"digit: $digit")
    status match {
      case Cleared => {
        status = EnteringLHS
        lhs = Some(digit.toString)
      }
      case EnteringLHS => lhs = lhs.map(_ + digit)
      case EnteringOper => {
        status = EnteringRHS
        rhs = Some(digit.toString)
      }
      case EnteringRHS => rhs = rhs.map(_ + digit)
      case (ShowingRes|ShowingInvalidRes) => {
        enterClear()
        enterDigit(digit)
      }
    }
    updateProps
    debug
  }
  def enterSeparator(): Unit = {
    logger.debug("separator")
    status match {
      case Cleared => {
        status = EnteringLHS
        lhs = Some("0.")
      }
      case EnteringLHS if (!lhs.get.matches(".*\\..*")) => lhs = lhs.map(_ + ".")
      case EnteringOper => {
        status = EnteringRHS
        rhs = Some("0.")
      }
      case EnteringRHS if (!rhs.get.matches(".*\\..*")) => rhs = rhs.map(_ + ".")
      case (ShowingRes|ShowingInvalidRes) => {
        enterClear()
        enterSeparator()
      }
      case _ => // Do nothing
    }
    updateProps
    debug
  }
  def enterPlusMinus():Unit = {
    logger.debug("plus/minus")
    status match {
      case Cleared => {
        status = EnteringLHS
        lhs = Some("-")
      }
      case (EnteringLHS|EnteringOper) => lhs = lhs.map(invertSign)
      case EnteringRHS => rhs = rhs.map(invertSign)
      case ShowingRes => {
        status = EnteringLHS
        lhs = Some(invertSign(result.value))
        rhs = None
        oper = None
      }
      case ShowingInvalidRes => {
        status = EnteringLHS
        lhs = Some("-")
        rhs = None
        oper = None
      }
    }
    updateProps
    debug

    def invertSign(number: String): String = {
      if (number.length > 0 && number.charAt(0) == '-') number.tail
      else "-" + number
    }
  }
  def enterOperator(operator: Operator):Unit = {
    logger.debug(s"operator: $operator")
    status match {
      case Cleared => {
        operator match {
          case Minus => enterPlusMinus()
          case _ => // Do nothing
        }
      }
      case (EnteringLHS|EnteringOper) => {
        if (lhs.get == "" || lhs.get == "-") lhs = lhs.map(_ + "0")
        status = EnteringOper
        oper = Some(operator)
      }
      case (EnteringRHS) => // TODO
      case ShowingRes => {
        status = EnteringOper
        lhs = Some(result.value)
        rhs = None
        oper = Some(operator)
      }
      case ShowingInvalidRes => {
        status = EnteringOper
        rhs = None
        oper = Some(operator)
      }
    }
    updateProps
    debug
  }
  def enterEquals():Unit = {
    logger.debug("equals")
    status match {
      case EnteringRHS => {
        status = ShowingRes
      }
      case _ => // Do nothing
    }
    updateProps
    debug
  }
  def enterClear():Unit = {
    logger.debug("clear")
    status = Cleared
    lhs = None
    rhs = None
    oper = None
    updateProps
    debug
  }

  // Properties
  def operation: ReadOnlyStringProperty = _operation.readOnlyProperty
  def getOperation: String = _operation.value
  def result: ReadOnlyStringProperty = _result.readOnlyProperty
  def getResult: String = _result.value

  // Update display properties
  private def updateProps: Unit = {
    _operation.value = lhs.getOrElse("").toString + oper.getOrElse("") + rhs.getOrElse("")
    _result.value =
      if (status == ShowingRes) calculate(lhs.get, rhs.get, oper.get)
      else ""

    def calculate(lhs: String, rhs: String, oper: Operator): String = {
      val l = BigDecimal(lhs)
      val r = BigDecimal(rhs)
      oper match {
        case Plus => (l + r).toString
        case Minus => (l - r).toString
        case Multiply => (l * r).toString
        case Divide =>
          if (r != 0) (l / r).toString
          else {
            status = ShowingInvalidRes
            "Division by 0"
          }
      }
    }
  }

  // debug
  private def debug:Unit = {
    logger.debug(
      s"""|
          |###############
          |status: $status
          |lhs: $lhs
          |rhs: $rhs
          |oper: $oper
          |operation ${_operation.value}
          |result ${_result.value}
          |###############""".stripMargin
    )
  }
}
