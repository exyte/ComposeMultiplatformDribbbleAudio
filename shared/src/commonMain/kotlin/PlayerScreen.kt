import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import albums.AlbumScreen
import androidx.compose.desktop.ui.tooling.preview.Preview
import com.exyte.composesample.ui.theme.PlayerTheme
import nowplaying.ANIM_DURATION
import nowplaying.NowPlayingAlbumScreen
import mainplayer.album.Background
import states.Screen
import states.ToNowPlaying
import states.rememberPlayerScreenState
import kotlinx.coroutines.launch
import mainplayer.MainPlayerScreen
import ui.BackHandler
import kotlin.math.abs

@Composable
fun PlayerScreen(playbackData: PlaybackData) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val screenState = rememberPlayerScreenState(constraints, LocalDensity.current)

        var toNowPlayingTransition by remember { mutableStateOf(ToNowPlaying.None) }

        val animScope = rememberCoroutineScope()

        var sharedElementTransitioned by remember { mutableStateOf(false) }
        var sharedElementParams by remember { mutableStateOf(SharedElementData.NONE) }

        val titleProgressForward = remember { Animatable(0f) }
        val sharedProgress = titleProgressForward.value

        fun animateOffset(initialValue: Float, targetValue: Float, onEnd: () -> Unit) {
            val distance = abs(targetValue - initialValue)
            val distancePercent = distance / screenState.maxContentWidth
            val duration = (250 * distancePercent).toInt()

            animScope.launch {
                animate(
                    initialValue = initialValue,
                    targetValue = targetValue,
                    animationSpec = tween(duration),
                ) { value, _ -> screenState.currentDragOffset = value }
                onEnd()
            }
        }

        fun collapse() {
            animateOffset(
                initialValue = screenState.currentDragOffset, targetValue = 0f
            ) {
                screenState.currentScreen = Screen.Player
            }
        }

        fun animateTitleProgress(targetValue: Float) {
            animScope.launch {
                titleProgressForward.animateTo(
                    targetValue = targetValue,
                    animationSpec = tween(ANIM_DURATION),
                )
            }
        }

        val goBackFromNowPlayingScreen: () -> Unit = remember {
            {
                sharedElementTransitioned = false
                animateTitleProgress(0f)
            }
        }

        Background(
            modifier = Modifier.align(Alignment.BottomCenter), screenState = screenState
        )

        if (screenState.currentScreen != Screen.Player) {
            val density = LocalDensity.current
            AlbumScreen(
                screenState = screenState,
                playbackData = playbackData,
                sharedProgress = sharedProgress,
                onBackClick = {
                    screenState.currentScreen = Screen.TransitionToComments
                    collapse()
                },
                onInfoClick = { data, x, y, size ->
                    sharedElementParams = SharedElementData(
                        data, x.toDp(density), y.toDp(density), size.toDp(density)
                    )
                    sharedElementTransitioned = true
                    toNowPlayingTransition = ToNowPlaying.Stable
                    animateTitleProgress(1f)
                },
            )
        }

        if (screenState.currentScreen != Screen.Comments) {
            MainPlayerScreen(
                screenState = screenState,
            )
        }

        if (toNowPlayingTransition == ToNowPlaying.Stable) {
            NowPlayingAlbumScreen(
                maxContentWidth = screenState.maxContentWidth,
                sharedElementParams = sharedElementParams,
                transitioned = sharedElementTransitioned,
                topInset = defaultStatusBarPadding,
                onTransitionFinished = {
                    if (!sharedElementTransitioned) {
                        toNowPlayingTransition = ToNowPlaying.None
                    }
                },
                onBackClick = goBackFromNowPlayingScreen
            )
        }
        println(screenState.backHandlerEnabled)
        BackHandler(isEnabled = screenState.backHandlerEnabled) {
            when {
                sharedElementTransitioned -> goBackFromNowPlayingScreen()
                screenState.currentScreen != Screen.Player -> {
                    screenState.currentScreen = Screen.TransitionToComments
                    collapse()
                }
            }
        }
    }
}

@Preview
@Composable
fun SomePreview(){
    PlayerTheme {
        PlayerScreen(PlaybackData())
    }
}