package guilgaly.fxtest.calculator

sealed trait Operator
case object Plus extends Operator {
  override def toString: String = "+"
}
case object Minus extends Operator {
  override def toString: String = "-"
}
case object Multiply extends Operator {
  override def toString: String = "×"
}
case object Divide extends Operator {
  override def toString: String = "÷"
}
