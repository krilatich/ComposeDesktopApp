package ui.theme

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

//region TopAppBar
val Colors.topAppBarBackground: Color
    @Composable get() = MaterialTheme.colors.primary

val Colors.topAppBarContent: Color
    @Composable get() = MaterialTheme.colors.onPrimary
//endregion
