package guilgaly.fxtest.mp3player

import com.escalatesoft.subcut.inject._

/**
 * Subcut configuration for the MP3 Player application.
 */
class Mp3PlayerConfiguration extends NewBindingModule(module => {
  import module._

  /** MP3 Player used for playing audio files. */
  bind[Mp3Player] toSingle(new Mp3Player)

})
