package guilgaly.fxtest.calculator

import com.escalatesoft.subcut.inject._

class CalculatorConfiguration extends NewBindingModule(module => {
  import module._

  bind[Calculator] toModuleSingle { implicit module => new Calculator }
})
