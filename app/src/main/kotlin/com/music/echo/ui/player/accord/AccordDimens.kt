package echo.music.iad1tya.ui.player.accord

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Layout constants ported from Accord's player resources
 * (values/dimens.xml, layout_full_player.xml,
 * layout_preview_player.xml, layout_full_player_tool_bar.xml).
 */
object AccordDimens {
  // Full player cover (full_cover_radius = 14dp)
  val CoverMarginHorizontal = 24.dp
  val CoverMarginTop = 18.dp
  val CoverCorner = 14.dp
  val CoverElevation = 24.dp

  // Glow behind the cover (Accord BlendView 170dp)
  val BlendGlowSize = 170.dp

  // Full title block
  val TitleMarginTop = 52.dp
  val TitleMarginStart = 36.dp
  val TitleMarginEnd = 16.dp
  val TitleFontSize = 21.sp
  val SubtitleFontSize = 21.sp
  val TitleEndButtonSize = 32.dp
  val TitleEndMargin = 36.dp
  val StarMarginEnd = 14.dp

  // Toolbar header (layout_full_player_tool_bar)
  val ToolbarCoverSize = 74.dp
  val ToolbarCoverCorner = 5.dp
  val ToolbarCoverMarginStart = 32.dp
  val ToolbarCoverMarginTop = 20.dp
  val ToolbarTitleFontSize = 18.sp
  val ToolbarSubtitleFontSize = 16.sp

  // Progress slider
  val SliderMarginHorizontal = 20.dp
  val SliderMarginTop = 32.dp
  val SliderHeight = 28.dp
  val TimestampFontSize = 12.5.sp

  // Transport (Accord: main 100dp/76dp icon, side 94dp/64dp)
  val MainControlSize = 100.dp
  val MainControlIconSize = 76.dp
  val SideControlSize = 94.dp
  val SideControlIconSize = 64.dp

  // Bottom toolbar (caption/airplay/list, 48dp, pinned 48dp above bottom)
  val ToolbarButtonSize = 48.dp
  val ToolbarIconSize = 36.dp
  val ToolbarMarginBottom = 48.dp

  // Mini (preview) player (preview_player_height = 58dp)
  val MiniHeight = 58.dp
  val MiniHorizontalMargin = 12.dp
  val MiniPanelCorner = 16.dp
  val MiniCoverCorner = 4.dp
  val MiniCoverMargin = 8.dp
  val MiniTitleFontSize = 16.sp
  val MiniButtonSize = 54.dp
  val MiniButtonIconSize = 20.dp

  // Mini -> full morph threshold on sheet progress (0f collapsed, 1f expanded)
  const val FullPlayerThreshold = 0.5f
}
