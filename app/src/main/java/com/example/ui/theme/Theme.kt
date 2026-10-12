package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val NeumorphicInkColorScheme = lightColorScheme(
    primary = InkBlack,
    onPrimary = PaperSurface,
    primaryContainer = PaperCard,
    onPrimaryContainer = InkBlack,
    secondary = GhostInk,
    onSecondary = PaperSurface,
    secondaryContainer = PaperDebossed,
    onSecondaryContainer = InkBlack,
    tertiary = InkBlack,
    onTertiary = PaperSurface,
    background = PaperBase,
    onBackground = InkBlack,
    surface = PaperSurface,
    onSurface = InkBlack,
    surfaceVariant = PaperCard,
    onSurfaceVariant = GhostInk,
    outline = FaintInk,
    outlineVariant = HairlineInk
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = NeumorphicInkColorScheme,
        typography = Typography,
        content = content
    )
}

