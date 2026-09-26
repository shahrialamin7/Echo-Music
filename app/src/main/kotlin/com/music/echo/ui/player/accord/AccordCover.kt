package echo.music.iad1tya.ui.player.accord

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import echo.music.iad1tya.LocalPlayerConnection

/**
 * Accord cover block (Phase 4, part 1).
 *
 * Ports Accord's full-player cover + BlendView: 1:1 artwork with
 * 28dp corners and 24dp elevation, with a blurred copy of the same
 * artwork glowing behind it. Artwork source is Echo's
 * Artwork source is Echo's MediaMetadata.thumbnailUrl, so no playback logic is duplicated.
 */
@Composable
fun AccordCover(modifier: Modifier = Modifier) {
  val playerConnection = LocalPlayerConnection.current ?: return
  val mediaMetadata by playerConnection.mediaMetadata.collectAsState()
  val artworkUrl = mediaMetadata?.thumbnailUrl

  Box(
    modifier =
      modifier
        .fillMaxWidth()
        .padding(horizontal = AccordDimens.CoverMarginHorizontal)
        .aspectRatio(1f),
    contentAlignment = Alignment.Center
  ) {
    if (artworkUrl != null) {
      // Glow layer (Accord BlendView 170dp equivalent, full-bleed blur).
      AsyncImage(
        model = artworkUrl,
        contentDescription = null,
        modifier =
          Modifier.fillMaxSize()
            .blur(100.dp)
            .graphicsLayer { alpha = AccordPlayerTheme.GlowAlpha },
        contentScale = ContentScale.Crop
      )
      // Foreground cover (Accord: 24dp elevation, full_cover_radius).
      AsyncImage(
        model = artworkUrl,
        contentDescription = null,
        modifier =
          Modifier.fillMaxSize()
            .offset(y = AccordDimens.CoverMarginTop)
            .shadow(AccordDimens.CoverElevation, AccordPlayerTheme.CoverShape)
            .clip(AccordPlayerTheme.CoverShape),
        contentScale = ContentScale.Crop
      )
    } else {
      Box(
        modifier =
          Modifier.fillMaxSize()
            .offset(y = AccordDimens.CoverMarginTop)
            .shadow(AccordDimens.CoverElevation, AccordPlayerTheme.CoverShape)
            .clip(AccordPlayerTheme.CoverShape)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
      )
    }
  }
}
