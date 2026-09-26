package echo.music.iad1tya.ui.player.accord

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import echo.music.iad1tya.LocalPlayerConnection
import echo.music.iad1tya.R

/**
 * Accord-style mini (preview) player.
 *
 * Ports Accord's PreviewPlayer + layout_preview_player: 58dp rounded
 * panel with 12dp side margins, square cover (4dp corners), 16sp
 * Inter medium title, 54dp prop play/next buttons. State comes from
 * Echo's PlayerConnection, so playback logic is untouched.
 */
@Composable
fun AccordMiniPlayer(modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
  val playerConnection = LocalPlayerConnection.current ?: return
  val mediaMetadata by playerConnection.mediaMetadata.collectAsState()
  val isPlaying by playerConnection.isPlaying.collectAsState()
  val title = mediaMetadata?.title.orEmpty()
  val artist = mediaMetadata?.artists?.joinToString { it.name }.orEmpty()
  val thumbnailUrl = mediaMetadata?.thumbnailUrl

  Box(
    modifier =
      modifier
        .fillMaxWidth()
        .padding(horizontal = AccordDimens.MiniHorizontalMargin)
        .clip(AccordPlayerTheme.MiniPanelShape)
        .background(MaterialTheme.colorScheme.surfaceContainer)
        .clickable(onClick = onClick)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth().height(AccordDimens.MiniHeight),
      verticalAlignment = Alignment.CenterVertically
    ) {
      if (thumbnailUrl != null) {
        AsyncImage(
          model = thumbnailUrl,
          contentDescription = null,
          contentScale = ContentScale.Crop,
          modifier =
            Modifier.padding(AccordDimens.MiniCoverMargin)
              .size(AccordDimens.MiniHeight - AccordDimens.MiniCoverMargin * 2)
              .clip(AccordPlayerTheme.MiniCoverShape)
        )
      } else {
        Box(
          modifier =
            Modifier.padding(AccordDimens.MiniCoverMargin)
              .size(AccordDimens.MiniHeight - AccordDimens.MiniCoverMargin * 2)
              .clip(AccordPlayerTheme.MiniCoverShape)
              .background(MaterialTheme.colorScheme.surfaceVariant)
        )
      }

      Column(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
        Text(
          text = title,
          fontFamily = AccordPlayerTheme.Inter,
          fontWeight = FontWeight.Medium,
          fontSize = AccordDimens.MiniTitleFontSize,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
        Text(
          text = artist,
          fontFamily = AccordPlayerTheme.Inter,
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
          painter =
            painterResource(
              if (isPlaying) R.drawable.accord_ic_prop_pause else R.drawable.accord_ic_prop_play
            ),
          contentDescription = null,
          tint = MaterialTheme.colorScheme.onSurface,
          modifier = Modifier.size(AccordDimens.MiniButtonIconSize)
        )
      }

      IconButton(
        onClick = { playerConnection.seekToNext() },
        modifier = Modifier.size(AccordDimens.MiniButtonSize)
      ) {
        Icon(
          painter = painterResource(R.drawable.accord_ic_prop_next),
          contentDescription = null,
          tint = MaterialTheme.colorScheme.onSurface,
          modifier = Modifier.size(AccordDimens.MiniButtonIconSize)
        )
      }
    }
  }
}
