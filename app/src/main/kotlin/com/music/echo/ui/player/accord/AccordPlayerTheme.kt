package echo.music.iad1tya.ui.player.accord

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

/**
 * Accord player visual tokens.
 *
 * Colors are derived from the host MaterialTheme colorScheme at the
 * call site (Accord itself uses overlay layers); this file only holds
 * shape / alpha constants so the theme stays dynamic like Echo.
 */
object AccordPlayerTheme {
  val CoverShape = RoundedCornerShape(28.dp)
  val MiniCoverShape = RoundedCornerShape(AccordDimens.MiniCoverCorner)
  val CardShape = RoundedCornerShape(24.dp)

  /** Translucent card fill, mirrors Echo's custom aesthetic. */
  const val TranslucentAlpha = 0.3f

  /** Glow behind the cover. Disable on low-end devices if it janks. */
  const val GlowAlpha = 0.55f
}
