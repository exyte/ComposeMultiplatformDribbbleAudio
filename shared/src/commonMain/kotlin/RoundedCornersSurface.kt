import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

const val CORNERS_SIZE = 48

@Composable
fun RoundedCornersSurface(
    modifier: Modifier = Modifier,
    topPadding: Dp = 0.dp,
    elevation: Dp = 0.dp,
    color: Color = MaterialTheme.colors.surface,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = elevation,
                shape = RoundedCornerShape(
                    bottomStart = CORNERS_SIZE.dp,
                    bottomEnd = CORNERS_SIZE.dp
                ),
                clip = true
            )
            .background(color)
            .padding(top = topPadding),
        content = content
    )
}