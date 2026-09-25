package echo.music.iad1tya.ui.player.accord

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Layout constants ported from Accord's XML player layouts
 * (layout_full_player.xml / layout_preview_player.xml).
 *
 * These mirror the original dimens so the Compose re-implementation
 * stays visually aligned with Accord. Streaming-only adaptations
 * are documented inline.
 */
object AccordDimens {
  // Full player cover
  val CoverMarginHorizontal = 24.dp
  val CoverMarginTop = 18.dp
  val CoverElevation = 24.dp

  // Glow behind the cover (Accord BlendView 170dp)
  val BlendGlowSize = 170.dp

  // Title block
  val TitleMarginTop = 52.dp
  val TitleMarginStart = 36.dp
  val TitleMarginEnd = 16.dp
  val TitleFontSize = 21.sp
  val SubtitleFontSize = 21.sp

  // Progress slider
  val SliderMarginHorizontal = 20.dp
  val SliderMarginTop = 32.dp
  val SliderHeight = 28.dp

  // Bottom control row (Accord OverlayButton 48dp)
  val ControlButtonSize = 48.dp
  val ControlIconSize = 36.dp
  val ControlRowMarginBottom = 48.dp

  // Mini (preview) player
  val MiniCoverCorner = 4.dp
  val MiniCoverMargin = 8.dp
  val MiniTitleFontSize = 16.sp
  val MiniButtonSize = 54.dp
  val MiniButtonIconSize = 20.dp

  // Mini -> full morph threshold on sheet progress (0f collapsed, 1f expanded)
  const val FullPlayerThreshold = 0.5f
}
