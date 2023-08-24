package albums

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.updateTransition
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

enum class Action {
    Back, Like, Flag, Delete,
}

data class ActionItem(
    val iconResId: String,
    val action: Action,
    val description: String,
)

private val icons = listOf(
    ActionItem("ic_back.xml", Action.Back, "Back"),
    ActionItem("ic_favourite_outline.xml", Action.Like, "Like"),
    ActionItem("ic_flag.xml", Action.Flag, "Flag"),
    ActionItem("ic_delete.xml", Action.Delete, "Delete"),
)

 const val CORNER_DURATION = 180
 const val EXPAND_DURATION = 120
 const val MIN_RADIUS = 0

@Composable
fun ActionPanel(
    modifier: Modifier = Modifier,
    maxWidth: Dp = 200.dp,
    maxHeight: Dp = 50.dp,
    isVisible: Boolean = false,
    onActionClick: (Action) -> Unit = {},
) {
    val stateTransition = updateTransition(targetState = isVisible, label = "Visibility")

    val targetWidth = stateTransition.animateDp(
        transitionSpec = {
            keyframes {
                durationMillis = CORNER_DURATION + EXPAND_DURATION
                if (targetState) {
                    maxHeight at CORNER_DURATION
                    maxWidth at CORNER_DURATION + EXPAND_DURATION
                } else {
                    maxHeight at EXPAND_DURATION
                    MIN_RADIUS.dp at EXPAND_DURATION + CORNER_DURATION
                }
            }
        },
        label = "Width"
    ) { value -> if (value) maxWidth else MIN_RADIUS.dp }

    val targetHeight by stateTransition.animateDp(
        transitionSpec = {
            keyframes {
                durationMillis = CORNER_DURATION + EXPAND_DURATION
                if (targetState) {
                    maxHeight at CORNER_DURATION
                } else {
                    maxHeight at EXPAND_DURATION
                    MIN_RADIUS.dp at EXPAND_DURATION + CORNER_DURATION
                }
            }
        },
        label = "Height"
    ) { value -> if (value) maxHeight else MIN_RADIUS.dp }

    val targetElevation by remember(targetWidth,maxWidth) {
        derivedStateOf {
            (2 * ((targetWidth.value - MIN_RADIUS.dp) / (maxWidth - MIN_RADIUS.dp))).dp
        }
    }

    val contentAlphaProvider = remember {
        { (targetWidth.value - MIN_RADIUS.dp) / (maxWidth - MIN_RADIUS.dp) }
    }

    ActionPanel(
        modifier = modifier,
        width = targetWidth.value,
        height = targetHeight,
        elevation = targetElevation,
        contentAlphaProvider = contentAlphaProvider,
        onActionClick = onActionClick,
    )
}

@Composable
fun ActionPanel(
    modifier: Modifier = Modifier,
    width: Dp,
    height: Dp,
    elevation: Dp,
    contentAlphaProvider: () -> Float,
    onActionClick: (Action) -> Unit,
) {
    Surface(
        modifier = modifier
            .size(width = width, height = height)
            .graphicsLayer { alpha = contentAlphaProvider() },
        elevation = elevation,
        color = MaterialTheme.colors.primaryVariant,
        shape = RoundedCornerShape(percent = 50),
    ) {
        Row(
            modifier = Modifier
                .size(width = width, height = height)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            icons.forEach { (iconId, action, description) ->
                ActionIconButton(
                    iconResId = iconId,
                    description = description,
                    action = action,
                    onClick = onActionClick,
                )
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun ActionIconButton(
    iconResId: String,
    description: String,
    action: Action,
    onClick: (Action) -> Unit = {},
) {
    IconButton(
        modifier = Modifier.size(32.dp),
        onClick = { onClick(action) },
    ) {
        Icon(
            painter = painterResource(iconResId),
            contentDescription = description,
            tint = Color.White,
        )
    }
}

@Preview
@Composable
fun ActionPanelPreview(){
    ActionPanel(onActionClick = {})
}