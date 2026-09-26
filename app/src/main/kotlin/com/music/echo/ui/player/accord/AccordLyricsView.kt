package echo.music.iad1tya.ui.player.accord

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import echo.music.iad1tya.LocalPlayerConnection
import echo.music.iad1tya.R
import echo.music.iad1tya.ui.component.Lyrics

/**
 * Accord lyrics page (functional).
 *
 * Ports layout_full_player_lyrics: reuses Echo's synced Lyrics
 * composable (same providers, same tap-to-seek) inside Accord
 * styling, with a header and close button back to the main page.
 */
@Composable
fun AccordLyricsView(modifier: Modifier = Modifier, onClose: () -> Unit = {}) {
  val playerConnection = LocalPlayerConnection.current ?: return

  Column(modifier = modifier.fillMaxSize()) {
    Row(
      modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "Lyrics",
        fontFamily = AccordPlayerTheme.Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = AccordDimens.ToolbarTitleFontSize,
        modifier = Modifier.weight(1f)
      )
      IconButton(onClick = onClose, modifier = Modifier.size(AccordDimens.ToolbarButtonSize)) {
        Icon(
          painter = painterResource(R.drawable.accord_ic_chevron_small),
          contentDescription = null,
          tint = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.size(24.dp)
        )
      }
    }
    Lyrics(
      sliderPositionProvider = { playerConnection.player.currentPosition },
      modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
      showLyrics = true
    )
  }
}
