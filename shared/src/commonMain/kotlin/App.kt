import androidx.compose.runtime.Composable
import com.exyte.composesample.ui.theme.PlayerTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import states.createStore

val store = CoroutineScope(SupervisorJob()).createStore()
@Composable
fun App() {
    PlayerTheme {
        PlayerScreen(PlaybackData())
    }
}