package mainplayer.movingupsongpanel

import NowPlayingSong
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.exyte.composesample.ui.theme.PlayerTheme
import states.PlayerScreenState


@Composable
fun MovingUpSongPanel(
    modifier: Modifier,
    screenState: PlayerScreenState,
    content: @Composable () -> Unit,
) {
    Layout(content, modifier) { measurables, constraints ->
        layout(constraints.maxWidth, constraints.maxHeight) {

            val playerControlConstraints = constraints.copy(
                minHeight = screenState.playerContainerHeight,
                maxHeight = screenState.playerContainerHeight
            )

            val songInfoContainerConstraints = constraints.copy(
                minHeight = screenState.songInfoContainerHeight,
                maxHeight = screenState.songInfoContainerHeight,
            )

//            require(measurables.size == 2)

            val playerControlContainer = measurables[1]
            val songInfoContainer = measurables[0]

            songInfoContainer.measure(songInfoContainerConstraints).place(0, screenState.songInfoOffset.toInt())
            playerControlContainer.measure(playerControlConstraints).place(0, screenState.playerControlOffset.toInt())
        }
    }
}

@Preview
@Composable
fun MovingUpSongPanelPreview() {
    PlayerTheme {
        MovingUpSongPanel(
            modifier = Modifier,
            screenState = PlayerScreenState(constraints = Constraints.fixed(1000, 1500), density = LocalDensity.current),
            content = {
                SongInfoContainer(height = 200.dp)
                PlayerControlContainer(nowPlayingSong = NowPlayingSong())
            }
        )
    }
}