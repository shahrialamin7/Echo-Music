package echo.music.iad1tya.ui.player.accord

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import echo.music.iad1tya.LocalPlayerConnection
import echo.music.iad1tya.R
import echo.music.iad1tya.ui.component.BottomSheet
import echo.music.iad1tya.ui.component.BottomSheetState

private enum class AccordPage { MAIN, QUEUE, LYRICS }

/**
 * Accord-style player sheet.
 *
 * Uses Echo's BottomSheet (same anchors, drag, fling and dismiss
 * behavior as the default player) and only swaps the visuals,
 * following Accord's layout_full_player order: cover, title + star,
 * subtitle, slider, transport, bottom toolbar (lyrics / queue).
 * Sheet progress (0f collapsed, 1f expanded) mirrors Accord's
 * FloatingPanelLayout fraction model for the mini -> full morph.
 */
@Composable
fun AccordPlayerSheet(state: BottomSheetState, navController: NavController, pureBlack: Boolean) {
  val playerConnection = LocalPlayerConnection.current ?: return
  val mediaMetadata by playerConnection.mediaMetadata.collectAsState()
  val title = mediaMetadata?.title.orEmpty()
  val artist = mediaMetadata?.artists?.joinToString { it.name }.orEmpty()
  var page by remember { mutableStateOf(AccordPage.MAIN) }

  BottomSheet(
    state = state,
    background = {
      Box(
        modifier =
          Modifier.fillMaxSize()
            .background(
              if (pureBlack) androidx.compose.ui.graphics.Color.Black
              else MaterialTheme.colorScheme.surfaceContainer
            )
      )
    },
    onDismiss = {
      playerConnection.service.clearAutomix()
      playerConnection.player.stop()
      playerConnection.player.clearMediaItems()
      playerConnection.service.clearPersistedQueueFiles()
    },
    collapsedContent = { AccordMiniPlayer(onClick = { state.expandSoft() }) }
  ) {
    when (page) {
      AccordPage.QUEUE -> AccordQueueSheet(onClose = { page = AccordPage.MAIN })
      AccordPage.LYRICS -> AccordLyricsView(onClose = { page = AccordPage.MAIN })
      AccordPage.MAIN ->
        Column(
          modifier = Modifier.fillMaxSize().padding(top = AccordDimens.CoverMarginTop),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          AccordCover()
          Spacer(modifier = Modifier.height(AccordDimens.TitleMarginTop))
          Row(
            modifier =
              Modifier.fillMaxWidth()
                .padding(start = AccordDimens.TitleMarginStart, end = AccordDimens.TitleMarginEnd),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = title,
                fontFamily = AccordPlayerTheme.Inter,
                fontWeight = FontWeight.SemiBold,
                fontSize = AccordDimens.TitleFontSize,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
              Text(
                text = artist,
                fontFamily = AccordPlayerTheme.Inter,
                fontWeight = FontWeight.Normal,
                fontSize = AccordDimens.SubtitleFontSize,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
            }
            AccordLikeButton()
          }
          AccordSlider(modifier = Modifier.padding(top = AccordDimens.SliderMarginTop))
          Spacer(modifier = Modifier.height(8.dp))
          AccordControls()
          Spacer(modifier = Modifier.weight(1f))
          Row(
            modifier =
              Modifier.fillMaxWidth().padding(bottom = AccordDimens.ToolbarMarginBottom),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
              onClick = { page = AccordPage.LYRICS },
              modifier = Modifier.size(AccordDimens.ToolbarButtonSize)
            ) {
              Icon(
                painter =
                  painterResource(
                    if (page == AccordPage.LYRICS) R.drawable.accord_ic_quote_filled
                    else R.drawable.accord_ic_quote
                  ),
                contentDescription = "Lyrics",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(AccordDimens.ToolbarIconSize)
              )
            }
            IconButton(
              onClick = { state.collapseSoft() },
              modifier = Modifier.size(AccordDimens.ToolbarButtonSize)
            ) {
              Icon(
                painter = painterResource(R.drawable.accord_ic_chevron_small),
                contentDescription = "Collapse",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(AccordDimens.ToolbarIconSize)
              )
            }
            IconButton(
              onClick = { page = AccordPage.QUEUE },
              modifier = Modifier.size(AccordDimens.ToolbarButtonSize)
            ) {
              Icon(
                painter =
                  painterResource(
                    if (page == AccordPage.QUEUE) R.drawable.accord_ic_bulletin_filled
                    else R.drawable.accord_ic_bulletin
                  ),
                contentDescription = "Queue",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(AccordDimens.ToolbarIconSize)
              )
            }
            Spacer(modifier = Modifier.weight(1f))
          }
        }
    }
  }
}
