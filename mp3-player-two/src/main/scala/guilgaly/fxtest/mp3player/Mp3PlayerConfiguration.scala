package guilgaly.fxtest.mp3player

import com.escalatesoft.subcut.inject._

class Mp3PlayerConfiguration extends NewBindingModule(module => {
  import module._

  bind[Mp3Player] toModuleSingle { implicit module => new Mp3Player }
})
