import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun MainView() {
    val systemUiController: SystemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.isStatusBarVisible = false
        systemUiController.isSystemBarsVisible = false
    }
    App()
}