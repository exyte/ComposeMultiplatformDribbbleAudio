package ui

import androidx.compose.runtime.Composable

@Composable
expect fun BackHandler(isEnabled: Boolean, onBack: ()-> Unit)