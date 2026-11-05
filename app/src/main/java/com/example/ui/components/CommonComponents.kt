package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberPro
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.FacebookBlue
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.GlassBorderSubtle
import com.example.ui.theme.GlassFill
import com.example.ui.theme.InstagramPurple
import com.example.ui.theme.NavyDarkBackground
import com.example.ui.theme.NavySurface
import com.example.ui.theme.NavySurfaceVariant
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.ProBadgeGradient
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TwitterSlate
import com.example.ui.theme.VividPurple
import com.example.ui.theme.YouTubeRed

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    borderColor: Color = GlassBorder,
    backgroundColor: Color = GlassFill,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    val cardModifier = if (onClick != null) {
        modifier
            .clip(shape)
            .clickable { onClick() }
            .border(1.dp, borderColor, shape)
            .background(backgroundColor, shape)
    } else {
        modifier
            .clip(shape)
            .border(1.dp, borderColor, shape)
            .background(backgroundColor, shape)
    }

    Box(modifier = cardModifier) {
        content()
    }
}

@Composable
fun ProBadge(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(ProBadgeGradient)
            .padding(horizontal = 8.dp, vertical = 3.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Bolt,
                contentDescription = "PRO",
                tint = Color.Black,
                modifier = Modifier.size(12.dp)
            )
            Text(
                text = "PRO",
                color = Color.Black,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.8.sp
            )
        }
    }
}

@Composable
fun PlatformChip(
    platform: String,
    modifier: Modifier = Modifier
) {
    val (color, tag) = when (platform.lowercase()) {
        "youtube" -> Pair(YouTubeRed, "YouTube")
        "tiktok" -> Pair(TikTokCyan, "TikTok")
        "instagram" -> Pair(InstagramPurple, "Instagram")
        "facebook" -> Pair(FacebookBlue, "Facebook")
        "twitter" -> Pair(TwitterSlate, "X / Twitter")
        else -> Pair(CyanNeon, platform)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.2f))
            .border(0.8.dp, color.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = tag,
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun GlowingProgressBar(
    progress: Float, // 0 to 100
    modifier: Modifier = Modifier,
    height: Dp = 8.dp
) {
    val normalized = (progress / 100f).coerceIn(0f, 1f)

    val transition = rememberInfiniteTransition(label = "glow")
    val shimmerOffset by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(CircleShape)
            .background(NavySurfaceVariant)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction = normalized)
                .height(height)
                .clip(CircleShape)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            CyanNeon,
                            ElectricBlue,
                            NeonViolet,
                            VividPurple
                        )
                    )
                )
        )
    }
}
