package guilgaly.fxtest.calculator

sealed trait CalculatorStatus
object CalculatorStatus {
  case object Cleared extends CalculatorStatus
  case object EnteringLHS extends CalculatorStatus
  case object EnteringOper extends CalculatorStatus
  case object EnteringRHS extends CalculatorStatus
  case object ShowingRes extends CalculatorStatus
  case object ShowingInvalidRes extends CalculatorStatus
}