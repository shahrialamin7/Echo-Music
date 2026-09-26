package echo.music.iad1tya.ui.player.accord

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import echo.music.iad1tya.LocalPlayerConnection
import echo.music.iad1tya.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

/**
 * Accord progress slider (functional).
 *
 * Ports the feel of Accord's OverlaySlider: full-width track with time
 * labels. Position polls the ExoPlayer while playing (same pattern as
 * Echo's player) and seeks through PlayerConnection on release.
 */
@Composable
fun AccordSlider(modifier: Modifier = Modifier) {
  val playerConnection = LocalPlayerConnection.current ?: return
  val mediaId = playerConnection.mediaMetadata.collectAsState().value?.id

  var position by remember { mutableLongStateOf(0L) }
  var duration by remember { mutableLongStateOf(0L) }
  var sliderPosition by remember { mutableStateOf<Long?>(null) }
  val isPlaying by playerConnection.isPlaying.collectAsState()

  LaunchedEffect(mediaId) {
    position = playerConnection.player.currentPosition
    duration = playerConnection.player.duration.coerceAtLeast(0L)
  }
  LaunchedEffect(isPlaying) {
    if (!isPlaying) return@LaunchedEffect
    while (isActive) {
      delay(100)
      if (sliderPosition == null) {
        position = playerConnection.player.currentPosition
        duration = playerConnection.player.duration.coerceAtLeast(0L)
      }
    }
  }

  Column(modifier = modifier.fillMaxWidth().padding(horizontal = AccordDimens.SliderMarginHorizontal)) {
    Slider(
      value = (sliderPosition ?: position).toFloat(),
      onValueChange = { sliderPosition = it.toLong() },
      valueRange = 0f..duration.coerceAtLeast(1L).toFloat(),
      onValueChangeFinished = {
        sliderPosition?.let { playerConnection.seekTo(it) }
        sliderPosition = null
      },
      modifier = Modifier.fillMaxWidth().height(AccordDimens.SliderHeight)
    )
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
      Text(
        text = formatMillis(sliderPosition ?: position),
        fontFamily = AccordPlayerTheme.Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = AccordDimens.TimestampFontSize,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
      Text(
        text = formatMillis(duration),
        fontFamily = AccordPlayerTheme.Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = AccordDimens.TimestampFontSize,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}

private fun formatMillis(ms: Long): String {
  val totalSec = (ms.coerceAtLeast(0L) / 1000).toInt()
  return "%d:%02d".format(totalSec / 60, totalSec % 60)
}

/**
 * Accord transport controls (functional).
 *
 * Ports Accord's huge transport row: 100dp main play/pause with 76dp
 * icon flanked by 94dp previous/next with 64dp icons (StateAnimated /
 * AnimatedVector feel approximated with a press scale). Shuffle and
 * repeat live in the queue page header, like Accord.
 */
@Composable
fun AccordControls(modifier: Modifier = Modifier) {
  val playerConnection = LocalPlayerConnection.current ?: return
  val isPlaying by playerConnection.isPlaying.collectAsState()

  Row(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically
  ) {
    IconButton(
      onClick = { playerConnection.seekToPrevious() },
      modifier = Modifier.size(AccordDimens.SideControlSize)
    ) {
      Icon(
        painter = painterResource(R.drawable.accord_ic_skip_previous_filled),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.size(AccordDimens.SideControlIconSize)
      )
    }

    IconButton(
      onClick = { playerConnection.togglePlayPause() },
      modifier = Modifier.size(AccordDimens.MainControlSize)
    ) {
      Icon(
        painter =
          painterResource(
            if (isPlaying) R.drawable.accord_ic_pause_filled
            else R.drawable.accord_ic_play_arrow_filled
          ),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.size(AccordDimens.MainControlIconSize)
      )
    }

    IconButton(
      onClick = { playerConnection.seekToNext() },
      modifier = Modifier.size(AccordDimens.SideControlSize)
    ) {
      Icon(
        painter = painterResource(R.drawable.accord_ic_skip_next_filled),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.size(AccordDimens.SideControlIconSize)
      )
    }
  }
}

/**
 * Accord title-row star (like) button. Accord places a star beside the
 * title; it maps to Echo's library like toggle.
 */
@Composable
fun AccordLikeButton(modifier: Modifier = Modifier) {
  val playerConnection = LocalPlayerConnection.current ?: return
  val currentSong by playerConnection.currentSong.collectAsState(initial = null)
  val liked = currentSong?.song?.liked == true

  IconButton(onClick = { playerConnection.toggleLike() }, modifier = modifier.size(AccordDimens.TitleEndButtonSize)) {
    Icon(
      painter = painterResource(if (liked) R.drawable.favorite else R.drawable.favorite_border),
      contentDescription = null,
      tint = if (liked) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = Modifier.size(28.dp)
    )
  }
}
