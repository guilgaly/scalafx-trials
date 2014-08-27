package guilgaly.fxtest.calculator

/**
 * Created by guillaumegaly on 26/08/2014.
 */
sealed trait CalculatorStatus
case object Cleared extends CalculatorStatus
case object EnteringLHS extends CalculatorStatus
case object EnteringOper extends CalculatorStatus
case object EnteringRHS extends CalculatorStatus
case object ShowingRes extends CalculatorStatus
case object ShowingInvalidRes extends CalculatorStatus