import androidx.compose.ui.window.ComposeUIViewController
import states.Action

fun MainViewController() = ComposeUIViewController { App()}

fun onBackGesture() {
    store.send(Action.OnBackPressed)
}