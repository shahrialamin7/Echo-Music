package echo.music.iad1tya.ui.player.accord

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import echo.music.iad1tya.LocalPlayerConnection

/**
 * Accord-style mini (preview) player.
 *
 * Ports Accord's PreviewPlayer + layout_preview_player: square cover,
 * single-line 16sp title, 54dp play/next buttons. State comes from
 * Echo's PlayerConnection, so playback logic is untouched.
 */
@Composable
fun AccordMiniPlayer(modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
  val playerConnection = LocalPlayerConnection.current ?: return
  val mediaMetadata by playerConnection.mediaMetadata.collectAsState()
  val isPlaying by playerConnection.isPlaying.collectAsState()
  val title = mediaMetadata?.title.orEmpty()
  val artist = mediaMetadata?.artists?.joinToString { it.name }.orEmpty()

  Row(
    modifier = modifier.fillMaxWidth().clickable(onClick = onClick).padding(horizontal = 4.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Cover placeholder (Accord: SimpleImageView 1:1, 4dp corners).
    // Real artwork via Thumbnail.kt lands in Phase 4.
    Box(
      modifier =
        Modifier.padding(AccordDimens.MiniCoverMargin)
          .size(48.dp)
          .clip(AccordPlayerTheme.MiniCoverShape)
          .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    )

    Column(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
      Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontSize = AccordDimens.MiniTitleFontSize,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
      Text(
        text = artist,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )
    }

    IconButton(
      onClick = { playerConnection.togglePlayPause() },
      modifier = Modifier.size(AccordDimens.MiniButtonSize)
    ) {
      Icon(
        imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
        contentDescription = null,
        modifier = Modifier.size(AccordDimens.MiniButtonIconSize)
      )
    }

    IconButton(
      onClick = { playerConnection.seekToNext() },
      modifier = Modifier.size(AccordDimens.MiniButtonSize)
    ) {
      Icon(
        imageVector = Icons.Filled.SkipNext,
        contentDescription = null,
        modifier = Modifier.size(AccordDimens.MiniButtonIconSize)
      )
    }
  }
}

/** Collapsed-bound height hint for the host sheet. */
val AccordMiniPlayerHeight = 64.dp
