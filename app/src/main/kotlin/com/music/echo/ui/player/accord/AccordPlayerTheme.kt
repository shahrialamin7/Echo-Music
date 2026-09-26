package echo.music.iad1tya.ui.player.accord

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import echo.music.iad1tya.R

/**
 * Accord player visual tokens.
 *
 * Inter comes from Accord's own font resources (copied as
 * accord_inter_*); colors stay derived from the host MaterialTheme
 * colorScheme so the theme follows Echo light/dark mode.
 */
object AccordPlayerTheme {
  val Inter = FontFamily(
    Font(R.font.accord_inter_regular, FontWeight.Normal),
    Font(R.font.accord_inter_medium, FontWeight.Medium),
    Font(R.font.accord_inter_semibold, FontWeight.SemiBold)
  )

  val CoverShape = RoundedCornerShape(AccordDimens.CoverCorner)
  val MiniCoverShape = RoundedCornerShape(AccordDimens.MiniCoverCorner)
  val MiniPanelShape = RoundedCornerShape(AccordDimens.MiniPanelCorner)
  val ToolbarCoverShape = RoundedCornerShape(AccordDimens.ToolbarCoverCorner)

  /** Glow behind the cover. Disable on low-end devices if it janks. */
  const val GlowAlpha = 0.55f
}
