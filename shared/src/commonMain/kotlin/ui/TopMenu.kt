package com.exyte.composesample.ui

//import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
//import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.delay
//import androidx.compose.ui.tooling.preview.Preview
//import com.exyte.composesample.R
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

/*
 * Created by Exyte on 14.10.2021.
 */

@Composable
fun TopMenu(
    modifier: Modifier = Modifier,
    startIcon: @Composable RowScope.() -> Unit,
    title: @Composable (RowScope.() -> Unit)? = null,
    endIcon: @Composable (RowScope.() -> Unit)? = null,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        startIcon()

        if (title == null) {
            Spacer(modifier = Modifier.weight(1f))
        } else {
            title()
        }
        if (endIcon != null) {
            endIcon()
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun TopMenu(
    modifier: Modifier = Modifier,
    title: String = "",
    titleColor: Color = MaterialTheme.colors.onSurface,
    iconsTint: Color = MaterialTheme.colors.onSurface,
    endIconResId: String = "",
    onStartIconClick: () -> Unit = {},
    onEndIconClick: () -> Unit = {},
) {
    TopMenu(
        modifier = modifier,
        startIcon = {
            IconButton(onClick = onStartIconClick) {
                Image(
                    painter = painterResource("ic-arrow-back.xml"),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(iconsTint),
                )
            }
        },
        title = if (title.isNotEmpty()) {
            {
                Text(
                    text = title,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = titleColor,
                )
            }
        } else null,
        endIcon = if (endIconResId != "") {
            {
                IconButton(onClick = onEndIconClick) {
                    Image(
                        painter = painterResource(endIconResId),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(iconsTint),
                    )
                }
            }
        } else null
    )
}

//@Preview
//@Composable
//private fun PreviewTopMenu() {
//    PlayerTheme(darkTheme = false) {
//        TopMenu(
//            modifier = Modifier.background(MaterialTheme.colors.surface),
//            iconsTint = MaterialTheme.colors.onSurface,
//            title = "Now Playing",
//            endIconResId = R.drawable.ic_share
//        )
//    }
//}