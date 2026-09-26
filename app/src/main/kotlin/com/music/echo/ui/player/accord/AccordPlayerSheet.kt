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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import echo.music.iad1tya.LocalPlayerConnection
import echo.music.iad1tya.ui.component.BottomSheet
import echo.music.iad1tya.ui.component.BottomSheetState

/**
 * Accord-style player sheet.
 *
 * Uses Echo's BottomSheet (same anchors, drag, fling and dismiss
 * behavior as the default player) and only swaps the visuals:
 * collapsedContent is Accord's preview mini player, content is the
 * Accord full player (cover + title + slider + controls). Sheet
 * progress (0f collapsed, 1f expanded) mirrors Accord's
 * FloatingPanelLayout fraction model for the mini -> full morph.
 */
@Composable
fun AccordPlayerSheet(state: BottomSheetState, navController: NavController, pureBlack: Boolean) {
  val playerConnection = LocalPlayerConnection.current ?: return
  val mediaMetadata by playerConnection.mediaMetadata.collectAsState()
  val title = mediaMetadata?.title.orEmpty()
  val artist = mediaMetadata?.artists?.joinToString { it.name }.orEmpty()

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
    Column(
      modifier = Modifier.fillMaxSize().padding(top = 18.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      AccordCover()
      Spacer(modifier = Modifier.height(24.dp))
      Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = AccordDimens.TitleMarginStart),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
          Text(
            text = artist,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }
        AccordLikeButton()
      }
      Spacer(modifier = Modifier.height(8.dp))
      AccordSlider()
      Spacer(modifier = Modifier.height(8.dp))
      AccordControls()
      Spacer(modifier = Modifier.height(16.dp))
      // Queue + lyrics dock here next (AccordQueueSheet / AccordLyricsView).
      Button(onClick = { state.collapseSoft() }) { Text(text = "Collapse") }
    }
  }
}
