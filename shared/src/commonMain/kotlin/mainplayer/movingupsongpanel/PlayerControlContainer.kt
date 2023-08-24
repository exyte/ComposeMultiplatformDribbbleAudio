package mainplayer.movingupsongpanel

import NowPlayingSong
import RoundedCornersSurface
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.AnimatedText
import ui.playerprogressbar.ProgressBar
import com.exyte.composesample.ui.TopMenu
import com.exyte.composesample.ui.theme.PlayerTheme
import statusBarsPaddingWithOffset
import ui.Fonts

/*
 * Created by Exyte on 08.10.2021.
 */
@Composable
fun PlayerControlContainer(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    nowPlayingSong: NowPlayingSong,
) {
    RoundedCornersSurface(
        modifier = modifier,
        color = MaterialTheme.colors.primary,
        elevation = 5.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TopMenu(
                modifier = Modifier.statusBarsPaddingWithOffset(),
                iconsTint = MaterialTheme.colors.onPrimary,
                endIconResId = "ic_search.xml",
                onStartIconClick = onBackClick,
                onEndIconClick = onSearchClick,
            )
            Spacer(modifier = Modifier.weight(2f))
            ContentTitle(
                text = nowPlayingSong.Singer,
                animate = true
            )
            Spacer(modifier = Modifier.weight(3f))
            Box(
                modifier = Modifier
                    .size(width = 32.dp, height = 1.dp)
                    .background(
                        MaterialTheme.colors.onSecondary.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(1.dp)
                    )
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.weight(3f))
            ContentSubtitle(
                text = nowPlayingSong.Description,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 48.dp),
            )
            Spacer(modifier = Modifier.weight(4f).widthIn(min = 16.dp))
            ProgressBar(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth(1f),
                songDuration = nowPlayingSong.SongDuration
            )
            Spacer(modifier = Modifier.weight(4f).widthIn(min = 16.dp))
        }

    }
}

@Composable
private fun ContentTitle(modifier: Modifier = Modifier, text: String, animate: Boolean) {
    AnimatedText(
        modifier = modifier,
        text = text,
        useAnimation = animate,
        style = MaterialTheme.typography.h4,
        fontFamily = Fonts.abrilFontFamily(),
        textColor = MaterialTheme.colors.contentColorFor(MaterialTheme.colors.primary),
    )
}

@Composable
private fun ContentSubtitle(text: String, modifier: Modifier) {
    Text(
        text = text,
        color = MaterialTheme.colors.contentColorFor(MaterialTheme.colors.primary),
        style = MaterialTheme.typography.body2,
        textAlign = TextAlign.Center,
        modifier = modifier,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        overflow = TextOverflow.Ellipsis,
        maxLines = 3,
    )
}

@Preview
@Composable
fun PlayerControlContainerPreview() {
    PlayerTheme {
        PlayerControlContainer(
            modifier = Modifier.height(500.dp),
            nowPlayingSong = NowPlayingSong()
        )
    }
}