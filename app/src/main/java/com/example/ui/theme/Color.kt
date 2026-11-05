package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Navy & Obsidian Backgrounds
val NavyDarkBackground = Color(0xFF070A13)
val NavySurface = Color(0xFF0D1424)
val NavySurfaceVariant = Color(0xFF141F36)
val NavySurfaceContainer = Color(0xFF18233C)
val NavySurfaceElevated = Color(0xFF1E2D4E)

// Neon & Accent Colors
val CyanNeon = Color(0xFF00E5FF)
val ElectricBlue = Color(0xFF2563EB)
val VividPurple = Color(0xFF9333EA)
val NeonViolet = Color(0xFFC084FC)
val HotMagenta = Color(0xFFF43F5E)
val AmberPro = Color(0xFFF59E0B)
val EmeraldSuccess = Color(0xFF10B981)

// Platform Colors
val YouTubeRed = Color(0xFFFF0000)
val TikTokCyan = Color(0xFF00F2FE)
val InstagramPurple = Color(0xFFE1306C)
val FacebookBlue = Color(0xFF1877F2)
val TwitterSlate = Color(0xFF1DA1F2)

// Text Colors
val TextPrimary = Color(0xFFF8FAFC)
val TextSecondary = Color(0xFF94A3B8)
val TextMuted = Color(0xFF64748B)

// Glass Borders & Tints
val GlassBorder = Color(0x3338BDF8)
val GlassBorderSubtle = Color(0x1FFFFFFF)
val GlassFill = Color(0x990D1424)
val GlassHighlight = Color(0x1AFFFFFF)

// Gradients
val PrimaryGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFF00E5FF), Color(0xFF3B82F6), Color(0xFF8B5CF6))
)

val AccentGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF3B82F6), Color(0xFF8B5CF6), Color(0xFFEC4899))
)

val ProBadgeGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFFF59E0B), Color(0xFFD97706), Color(0xFFFBBF24))
)

val CardGlowGradient = Brush.radialGradient(
    colors = listOf(Color(0x332563EB), Color(0x00000000))
)
