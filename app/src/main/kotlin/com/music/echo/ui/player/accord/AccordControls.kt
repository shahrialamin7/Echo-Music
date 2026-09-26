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
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
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
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
      Text(
        text = formatMillis(duration),
        style = MaterialTheme.typography.bodySmall,
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
 * 48dp icon row in Accord's OverlayButton spirit: shuffle, previous,
 * large play/pause, next, repeat (OFF -> ALL -> ONE via Media3 helper).
 * State mirrors playerConnection flows, so no logic is duplicated.
 */
@Composable
fun AccordControls(modifier: Modifier = Modifier) {
  val playerConnection = LocalPlayerConnection.current ?: return
  val isPlaying by playerConnection.isPlaying.collectAsState()
  val shuffleEnabled by playerConnection.shuffleModeEnabled.collectAsState()
  val repeatMode by playerConnection.repeatMode.collectAsState()

  Row(
    modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp),
    horizontalArrangement = Arrangement.SpaceEvenly,
    verticalAlignment = Alignment.CenterVertically
  ) {
    IconButton(
      onClick = { playerConnection.player.shuffleModeEnabled = !shuffleEnabled },
      modifier = Modifier.size(AccordDimens.ControlButtonSize)
    ) {
      Icon(
        painter = painterResource(if (shuffleEnabled) R.drawable.shuffle_on else R.drawable.shuffle),
        contentDescription = null,
        tint = if (shuffleEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.size(28.dp)
      )
    }

    IconButton(
      onClick = { playerConnection.seekToPrevious() },
      modifier = Modifier.size(56.dp)
    ) {
      Icon(
        painter = painterResource(R.drawable.skip_previous),
        contentDescription = null,
        modifier = Modifier.size(36.dp)
      )
    }

    IconButton(
      onClick = { playerConnection.togglePlayPause() },
      modifier = Modifier.size(72.dp)
    ) {
      Icon(
        painter = painterResource(if (isPlaying) R.drawable.pause else R.drawable.play),
        contentDescription = null,
        modifier = Modifier.size(44.dp)
      )
    }

    IconButton(
      onClick = { playerConnection.seekToNext() },
      modifier = Modifier.size(56.dp)
    ) {
      Icon(
        painter = painterResource(R.drawable.skip_next),
        contentDescription = null,
        modifier = Modifier.size(36.dp)
      )
    }

    IconButton(
      onClick = { playerConnection.player.toggleRepeatMode() },
      modifier = Modifier.size(AccordDimens.ControlButtonSize)
    ) {
      Icon(
        painter =
          painterResource(
            when (repeatMode) {
              Player.REPEAT_MODE_ONE -> R.drawable.repeat_one
              else -> R.drawable.repeat
            }
          ),
        contentDescription = null,
        tint = if (repeatMode != Player.REPEAT_MODE_OFF) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.size(28.dp)
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

  IconButton(onClick = { playerConnection.toggleLike() }, modifier = modifier.size(AccordDimens.ControlButtonSize)) {
    Icon(
      painter = painterResource(if (liked) R.drawable.favorite else R.drawable.favorite_border),
      contentDescription = null,
      tint = if (liked) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = Modifier.size(28.dp)
    )
  }
}
