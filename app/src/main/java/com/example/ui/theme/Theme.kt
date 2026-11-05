package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = CyanNeon,
    onPrimary = Color(0xFF001E2F),
    primaryContainer = ElectricBlue,
    onPrimaryContainer = Color(0xFFDBEAFE),
    secondary = VividPurple,
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFF3B0764),
    onSecondaryContainer = Color(0xFFF3E8FF),
    tertiary = HotMagenta,
    onTertiary = Color(0xFFFFFFFF),
    background = NavyDarkBackground,
    onBackground = TextPrimary,
    surface = NavySurface,
    onSurface = TextPrimary,
    surfaceVariant = NavySurfaceVariant,
    onSurfaceVariant = TextSecondary,
    surfaceContainer = NavySurfaceContainer,
    surfaceContainerHigh = NavySurfaceElevated,
    outline = GlassBorder,
    outlineVariant = GlassBorderSubtle
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
