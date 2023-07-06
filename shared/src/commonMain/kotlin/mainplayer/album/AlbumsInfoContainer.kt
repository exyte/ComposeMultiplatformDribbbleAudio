package mainplayer.album

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ScaleFactor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.exyte.composesample.ui.theme.Haiti
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import states.PlayerScreenState
import states.Screen
import kotlin.math.max


@Composable
fun Background(
    modifier: Modifier = Modifier,
    screenState: PlayerScreenState,
) {
    if (screenState.currentScreen != Screen.Comments) {
        AlbumInfoContainer(
            modifier = modifier
                .height(screenState.albumsInfoSize),
            photoScale = screenState.photoScale,
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun AlbumInfoContainer(
    modifier: Modifier,
    photoScale: Float = 1f,
    albumNumbers: Int = 12,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colors.surface,
    ) {
        Row {
            AlbumNumberContainerCustom(
                modifier = Modifier.weight(1f)
            ) {
                AlbumNumber(albumNumbers)

                Icon(
                    painter = painterResource("ic_layers.xml"),
                    contentDescription = null,
                    tint = MaterialTheme.colors.secondary,
                    modifier = Modifier
                        .size(24.dp),
                )
                Text(
                    text = AlbumsTitle,
                    fontSize = 14.sp,
                    modifier = Modifier,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colors.primary,
                )
                Text(
                    text = AlbumsTitle,
                    fontSize = 14.sp,
                    modifier = Modifier,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colors.primary,
                )
                Text(
                    text = AlbumsTitle,
                    fontSize = 14.sp,
                    modifier = Modifier,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colors.primary,
                )
            }

            Image(
                painter = painterResource("img_photo.webp"),
                contentDescription = null,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentScale = PhotoScale(photoScale)
            )
        }
    }
}

@Composable
fun Int.toSp() = with(LocalDensity.current) { this@toSp.toSp() }

const val AlbumsTitle = "Albums"
const val StartOffsetProportion = 0.3f
const val TopOffsetProportion = 0.7f
const val SmallAlbumNumberAdditionalOffset = 0.01f
const val SmallAlbumNumberTextDivider = 6
const val LargeAlbumNumberTextDivider = 2

@Composable
fun AlbumNumber(albumNumbers: Int) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    Box(
        modifier = Modifier.fillMaxSize().onGloballyPositioned {
            size = it.size
        }
    ) {
        val textSizeSmall by remember {
            derivedStateOf {
                (size.height / SmallAlbumNumberTextDivider)
            }
        }
        val textSizeLarge by remember {
            derivedStateOf {
                size.height / LargeAlbumNumberTextDivider
            }
        }
        val startTextOffset by remember {
            derivedStateOf {
                (size.width * StartOffsetProportion).toInt()
            }
        }
        val topTextOffset by remember {
            derivedStateOf {
                (size.height * TopOffsetProportion).toInt()
            }
        }
        Text(
            modifier = Modifier.offset {
                IntOffset(
                    x = startTextOffset,
                    y = topTextOffset - textSizeLarge
                )
            },
            fontWeight = FontWeight.Bold,
            text = albumNumbers.toString(),
            color = Haiti.copy(alpha = 0.07f),
            fontSize = textSizeLarge.toSp()
        )
        Text(
            modifier = Modifier.offset {
                IntOffset(
                    x = startTextOffset + SmallAlbumNumberAdditionalOffset.toInt(),
                    y = topTextOffset - textSizeSmall
                )
            },
            text = albumNumbers.toString(),
            fontWeight = FontWeight.Bold,
            color = Haiti,
            fontSize = textSizeSmall.toSp()
        )
    }
}

data class PhotoScale(val additionalScale: Float) : ContentScale {
    override fun computeScaleFactor(srcSize: Size, dstSize: Size): ScaleFactor {
        if (additionalScale > 1f) {
            val newWidth = dstSize.width * additionalScale
            val newHeight = dstSize.height * additionalScale
            return computeFillMaxDimension(srcSize, Size(newWidth, newHeight)).let {
                ScaleFactor(it, it)
            }
        }

        return ContentScale.Crop.computeScaleFactor(srcSize, dstSize)
    }

    private fun computeFillMaxDimension(srcSize: Size, dstSize: Size): Float {
        val widthScale = computeFillWidth(srcSize, dstSize)
        val heightScale = computeFillHeight(srcSize, dstSize)
        return max(widthScale, heightScale)
    }

    private fun computeFillWidth(srcSize: Size, dstSize: Size): Float =
        dstSize.width / srcSize.width

    private fun computeFillHeight(srcSize: Size, dstSize: Size): Float =
        dstSize.height / srcSize.height

}