package echo.music.iad1tya.ui.player.accord

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import coil3.compose.AsyncImage
import echo.music.iad1tya.LocalPlayerConnection
import echo.music.iad1tya.R
import echo.music.iad1tya.extensions.toggleRepeatMode

/**
 * Accord queue page (functional).
 *
 * Ports layout_full_player_queue: list with tap-to-play (same
 * seekToDefaultPosition pattern as Echo's Queue), drag reorder omitted
 * for now. Header carries shuffle + repeat toggles, like Accord's
 * queue header, plus a close button back to the main page.
 */
@Composable
fun AccordQueueSheet(modifier: Modifier = Modifier, onClose: () -> Unit = {}) {
  val playerConnection = LocalPlayerConnection.current ?: return
  val queueWindows by playerConnection.queueWindows.collectAsState()
  val currentWindowIndex by playerConnection.currentWindowIndex.collectAsState()
  val shuffleEnabled by playerConnection.shuffleModeEnabled.collectAsState()
  val repeatMode by playerConnection.repeatMode.collectAsState()

  Column(modifier = modifier.fillMaxSize()) {
    Row(
      modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "Queue (${queueWindows.size})",
        fontFamily = AccordPlayerTheme.Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = AccordDimens.ToolbarTitleFontSize,
        modifier = Modifier.weight(1f)
      )
      IconButton(
        onClick = { playerConnection.player.shuffleModeEnabled = !shuffleEnabled },
        modifier = Modifier.size(AccordDimens.ToolbarButtonSize)
      ) {
        Icon(
          painter = painterResource(R.drawable.accord_ic_nowplaying_shuffle),
          contentDescription = null,
          tint = if (shuffleEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.size(24.dp)
        )
      }
      IconButton(
        onClick = { playerConnection.player.toggleRepeatMode() },
        modifier = Modifier.size(AccordDimens.ToolbarButtonSize)
      ) {
        Icon(
          painter =
            painterResource(
              when (repeatMode) {
                Player.REPEAT_MODE_ONE -> R.drawable.accord_ic_nowplaying_repeat_one
                Player.REPEAT_MODE_ALL -> R.drawable.accord_ic_nowplaying_repeat
                else -> R.drawable.accord_ic_nowplaying_repeat
              }
            ),
          contentDescription = null,
          tint = if (repeatMode != Player.REPEAT_MODE_OFF) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.size(24.dp)
        )
      }
      IconButton(onClick = onClose, modifier = Modifier.size(AccordDimens.ToolbarButtonSize)) {
        Icon(
          painter = painterResource(R.drawable.accord_ic_chevron_small),
          contentDescription = null,
          tint = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.size(24.dp)
        )
      }
    }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp)) {
      itemsIndexed(queueWindows, key = { _, window -> window.uid }) { index, window ->
        AccordQueueRow(
          window = window,
          isCurrent = index == currentWindowIndex,
          onClick = {
            playerConnection.player.seekToDefaultPosition(window.firstPeriodIndex)
            playerConnection.player.playWhenReady = true
          }
        )
      }
    }
  }
}

@Composable
private fun AccordQueueRow(window: Timeline.Window, isCurrent: Boolean, onClick: () -> Unit) {
  val metadata = window.mediaItem.mediaMetadata
  Row(
    modifier =
      Modifier.fillMaxWidth()
        .clip(AccordPlayerTheme.MiniPanelShape)
        .background(
          if (isCurrent) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
          else MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.5f)
        )
        .clickable(onClick = onClick)
        .padding(8.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    val art = metadata?.artworkUri?.toString()
    if (art != null) {
      AsyncImage(
        model = art,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.size(48.dp).clip(AccordPlayerTheme.MiniCoverShape)
      )
    } else {
      Box(
        modifier =
          Modifier.size(48.dp)
            .clip(AccordPlayerTheme.MiniCoverShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
      )
    }
    Spacer(modifier = Modifier.width(12.dp))
    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = metadata?.title?.toString().orEmpty(),
        fontFamily = AccordPlayerTheme.Inter,
        fontWeight = if (isCurrent) FontWeight.SemiBold else FontWeight.Medium,
        fontSize = AccordDimens.MiniTitleFontSize,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
      )
      Text(
        text = metadata?.artist?.toString().orEmpty(),
        fontFamily = AccordPlayerTheme.Inter,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
    }
  }
  Spacer(modifier = Modifier.height(4.dp))
}
