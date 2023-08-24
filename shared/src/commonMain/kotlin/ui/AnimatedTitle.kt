@file:OptIn(ExperimentalTextApi::class)

package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import com.exyte.composesample.ui.theme.PlayerTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.roundToInt

private class FontWidthCalculator(val textMeasurer: TextMeasurer, val spanStyle: SpanStyle) {
    fun calculate(): Map<Char, Int> {

        val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz "
        val sizeMap = mutableMapOf<Char, Int>()
        alphabet.forEach { letter ->
            sizeMap[letter] = textMeasurer.measure(
                text = AnnotatedString(
                    text = letter.toString(),
                    spanStyle = spanStyle
                )
            ).size.width
        }
        return sizeMap
    }
}

@Composable
fun AnimatedText(
    modifier: Modifier = Modifier,
    text: String,
    useAnimation: Boolean = false,
    animationDelay: Long = 0L,
    style: TextStyle = MaterialTheme.typography.h5,
    fontFamily: FontFamily,
    textColor: Color = MaterialTheme.colors.onSurface,
) {


    val density = LocalDensity.current
    val spanStyle by remember {
        mutableStateOf(
            SpanStyle(
                color = textColor,
                fontStyle = style.fontStyle,
                fontSize = style.fontSize,
                fontFamily = fontFamily
            )
        )
    }

    val textMeasurer = rememberTextMeasurer()
    val textOffset = remember { with(density) { style.fontSize.toPx() / 2f } }
    val alphabet = remember { FontWidthCalculator(textMeasurer, spanStyle).calculate() }

    val initialOffset = remember { with(density) { 12.sp.toPx() } }
    val offsetStep = remember { initialOffset / 4f }
    var currentOffset by remember { mutableStateOf(initialOffset) }
    var nextOffset by remember { mutableStateOf(initialOffset) }
    var indexAnim by remember { mutableStateOf(0) }

    if (useAnimation) {
        LaunchedEffect(key1 = Unit) {
            launch {
                delay(animationDelay)
                while (indexAnim < text.length) {
                    val shouldSkip = text[indexAnim] == ' '
                    if (shouldSkip) {
                        indexAnim++
                        continue
                    }
                    while (currentOffset > 0f) {
                        delay(16)
                        currentOffset = max(0f, currentOffset - offsetStep)
                        nextOffset = max(0f, nextOffset - (offsetStep * 0.5f))
                    }
                    currentOffset = nextOffset
                    nextOffset = initialOffset
                    indexAnim++
                }
            }
        }
    }

    val annotatedString = remember {
        AnnotatedString(
            text = text,
            spanStyle = spanStyle
        )
    }

    Layout(
        modifier = modifier,
        content = {
            Canvas(modifier = modifier) {
                val height = size.height
                val baseOffset = height / 2f - textOffset
                if (useAnimation) {
                    val lastIndex = indexAnim
                    var x = 0
                    repeat(lastIndex) { x += alphabet[text[it]] ?: 0 }
                    drawIntoCanvas {
                        if (lastIndex > 0) {
                            drawText(
                                textLayoutResult = textMeasurer.measure(
                                    text = annotatedString.subSequence(0, lastIndex)
                                ),
                                topLeft = Offset(0f, baseOffset),
                                color = textColor
                            )
                        }
                        if (lastIndex < text.length) {
                            val firstAnimatedLetterOffsetProgress = if (currentOffset == 0f) {
                                1f
                            } else {
                                1f - currentOffset / initialOffset
                            }
                            drawText(
                                textLayoutResult = textMeasurer.measure(
                                    text = annotatedString.subSequence(lastIndex, lastIndex + 1)
                                ),
                                topLeft = Offset(x.toFloat(), baseOffset + currentOffset),
                                color = textColor.copy(firstAnimatedLetterOffsetProgress),
                            )
                            if (lastIndex + 1 < text.length) {
                                x += (alphabet[text[lastIndex]] ?: 0)
                                val secondAnimatedLetterOffsetProgress = if (nextOffset == 0f) {
                                    1f
                                } else {
                                    1f - nextOffset / initialOffset
                                }
                                drawText(
                                    textLayoutResult = textMeasurer.measure(
                                        text = annotatedString.subSequence(
                                            lastIndex + 1,
                                            lastIndex + 2
                                        )
                                    ),
                                    topLeft = Offset(x.toFloat(), baseOffset + nextOffset),
                                    color = textColor.copy(alpha = secondAnimatedLetterOffsetProgress)
                                )
                            }
                        }
                    }
                } else {
                    drawText(
                        textLayoutResult = textMeasurer.measure(text = annotatedString),
                        topLeft = Offset(0f, baseOffset),
                        color = textColor
                    )
                }
            }
        }
    ) { placeables, constraints ->
        val textWidth =
            text.fold(0f) { acc, char -> acc + (alphabet[char] ?: 0) }.roundToInt() + 1
        val textSize = (style.fontSize.value.sp * 1.2f).roundToPx()
        layout(textWidth, textSize) {
            val canvas = placeables.first()
            val cc = constraints.copy(
                minWidth = textWidth,
                maxWidth = textWidth,
                minHeight = textSize,
                maxHeight = textSize
            )
            canvas.measure(cc).placeRelative(x = 0, y = 0)
        }
    }
}

@Preview
@Composable
fun AnimatedTitlePreview() {
    PlayerTheme {
        AnimatedText(
            text = "Animated Text",
            useAnimation = false,
            fontFamily = Fonts.abrilFontFamily()
        )
    }
}