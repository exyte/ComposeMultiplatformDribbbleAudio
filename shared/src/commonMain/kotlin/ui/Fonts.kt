@file:OptIn(ExperimentalResourceApi::class)

package ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.resource

object Fonts {
    @Composable
    fun abrilFontFamily() = FontFamily(
        font(
            "Abril",
            "abril_fatface",
            FontWeight.Normal,
            FontStyle.Normal
        ),
    )
}

@Composable
expect fun font(name: String, res: String, weight: FontWeight, style: FontStyle): Font