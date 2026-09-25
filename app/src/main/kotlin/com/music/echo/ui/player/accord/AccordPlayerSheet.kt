package echo.music.iad1tya.ui.player.accord

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import echo.music.iad1tya.LocalPlayerConnection
import echo.music.iad1tya.ui.component.BottomSheetState

/**
 * Accord-style player sheet (scaffold).
 *
 * Mirrors Accord's FloatingPanelLayout fraction model: sheet [progress]
 * (0f collapsed, 1f expanded) drives the mini -> full morph. Full
 * Accord cover/slider/controls/queue/lyrics land here in Phase 4;
 * the scaffold only switches mini vs placeholder-full so the branch
 * plumbing is verifiable end-to-end.
 */
@Composable
fun AccordPlayerSheet(state: BottomSheetState, navController: NavController, pureBlack: Boolean) {
  val playerConnection = LocalPlayerConnection.current ?: return
  val mediaMetadata by playerConnection.mediaMetadata.collectAsState()
  val title = mediaMetadata?.title.orEmpty()
  val artist = mediaMetadata?.artists?.joinToString { it.name }.orEmpty()

  Box(modifier = Modifier.fillMaxSize()) {
    if (state.progress < AccordDimens.FullPlayerThreshold) {
      AccordMiniPlayer(
        modifier = Modifier.align(Alignment.BottomCenter),
        onClick = { state.expandSoft() }
      )
    } else {
      // Full player: Accord cover + title block so far; controls,
      // slider, queue and lyrics dock in here as Phase 4 lands.
      Column(
        modifier = Modifier.fillMaxSize().padding(top = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        AccordCover()
        Spacer(modifier = Modifier.height(AccordDimens.TitleMarginTop))
        Text(
          text = title,
          style = MaterialTheme.typography.headlineSmall,
          modifier = Modifier.padding(horizontal = AccordDimens.TitleMarginStart)
        )
        Text(
          text = artist,
          style = MaterialTheme.typography.bodyLarge,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { state.collapseSoft() }) { Text(text = "Collapse") }
      }
    }
  }
}
