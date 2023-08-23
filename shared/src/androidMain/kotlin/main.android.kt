import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.exyte.composesample.ui.theme.PlayerTheme
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

@Composable
fun SomeAndroidSpecificView() {
    Text(
        color = Color.White,
        text = "some android specific view"
    )
}

@Preview
@Composable
fun Preview() {
    PlayerTheme {
        SomeAndroidSpecificView()

    }
}