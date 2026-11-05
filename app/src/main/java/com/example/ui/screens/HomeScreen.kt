package com.example.ui.screens

import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.components.GlassCard
import com.example.ui.components.PlatformChip
import com.example.ui.components.ProBadge
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.FacebookBlue
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.GlassBorderSubtle
import com.example.ui.theme.GlassFill
import com.example.ui.theme.HotMagenta
import com.example.ui.theme.InstagramPurple
import com.example.ui.theme.NavyDarkBackground
import com.example.ui.theme.NavySurface
import com.example.ui.theme.NavySurfaceContainer
import com.example.ui.theme.NavySurfaceElevated
import com.example.ui.theme.NavySurfaceVariant
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.PrimaryGradient
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TwitterSlate
import com.example.ui.theme.VividPurple
import com.example.ui.theme.YouTubeRed
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.ScreenTab

data class PlatformCardData(
    val name: String,
    val subtitle: String,
    val iconColor: Color,
    val gradient: Brush,
    val badge: String
)

data class FeaturedVideoData(
    val title: String,
    val author: String,
    val platform: String,
    val duration: String,
    val thumbnail: String,
    val url: String
)

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val urlInput by viewModel.urlInput.collectAsState()
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()
    val activeDownloads by viewModel.activeDownloads.collectAsState()
    val completedDownloads by viewModel.completedDownloads.collectAsState()

    val platforms = listOf(
        PlatformCardData(
            name = "YouTube",
            subtitle = "4K / 1080p / Shorts / MP3",
            iconColor = YouTubeRed,
            gradient = Brush.linearGradient(listOf(Color(0xFF7F1D1D), Color(0xFF991B1B))),
            badge = "4K HDR"
        ),
        PlatformCardData(
            name = "TikTok",
            subtitle = "HD Video without Watermark",
            iconColor = TikTokCyan,
            gradient = Brush.linearGradient(listOf(Color(0xFF064E3B), Color(0xFF0F766E))),
            badge = "No Watermark"
        ),
        PlatformCardData(
            name = "Instagram",
            subtitle = "Reels, Stories & IGTV",
            iconColor = InstagramPurple,
            gradient = Brush.linearGradient(listOf(Color(0xFF701A75), Color(0xFF831843))),
            badge = "Reels"
        ),
        PlatformCardData(
            name = "Facebook",
            subtitle = "Public Watch & Videos",
            iconColor = FacebookBlue,
            gradient = Brush.linearGradient(listOf(Color(0xFF1E3A8A), Color(0xFF172554))),
            badge = "Watch"
        ),
        PlatformCardData(
            name = "Twitter",
            subtitle = "X High Bitrate Videos",
            iconColor = TwitterSlate,
            gradient = Brush.linearGradient(listOf(Color(0xFF1E293B), Color(0xFF0F172A))),
            badge = "HD"
        ),
        PlatformCardData(
            name = "Web Video",
            subtitle = "Direct Link Extractor",
            iconColor = CyanNeon,
            gradient = Brush.linearGradient(listOf(Color(0xFF0E7490), Color(0xFF155E75))),
            badge = "Universal"
        )
    )

    val featuredVideos = listOf(
        FeaturedVideoData(
            title = "Cyberpunk 2077 Night City 4K Ultra Cinematic HDR",
            author = "FutureTech Official",
            platform = "YouTube",
            duration = "08:42",
            thumbnail = "https://images.unsplash.com/photo-1542751371-adc38448a05e?w=800&q=80",
            url = "https://www.youtube.com/watch?v=Cyberpunk2077_HDR"
        ),
        FeaturedVideoData(
            title = "Insane cinematic drone glide through futuristic Tokyo neon streets",
            author = "@neon_explorer",
            platform = "TikTok",
            duration = "00:45",
            thumbnail = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=800&q=80",
            url = "https://www.tiktok.com/@neon_explorer/video/7289382103"
        ),
        FeaturedVideoData(
            title = "Hyper-realistic VFX Breakdown & Color Grading Masterclass",
            author = "@motion_creatives",
            platform = "Instagram",
            duration = "01:15",
            thumbnail = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=800&q=80",
            url = "https://www.instagram.com/reel/C892_VFX_Art"
        ),
        FeaturedVideoData(
            title = "Deep Sea Discovery: Bioluminescent Creatures in 4K HDR",
            author = "Earth Odyssey Watch",
            platform = "Facebook",
            duration = "12:30",
            thumbnail = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=800&q=80",
            url = "https://www.facebook.com/watch/?v=9823412098"
        )
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NavyDarkBackground),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // App Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Brush.radialGradient(listOf(CyanNeon.copy(alpha = 0.3f), Color.Transparent)))
                            .border(1.5.dp, CyanNeon, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Logo",
                            tint = CyanNeon,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "SEARX AI",
                                color = TextPrimary,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            ProBadge()
                        }
                        Text(
                            text = "Universal Video Downloader",
                            color = CyanNeon,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { viewModel.navigateTo(ScreenTab.SETTINGS) },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(NavySurfaceVariant)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = TextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Live Stats Strip
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 16.dp,
                backgroundColor = NavySurfaceContainer,
                borderColor = GlassBorderSubtle
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { viewModel.navigateTo(ScreenTab.DOWNLOADS) }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (activeDownloads.isNotEmpty()) CyanNeon else Color.Gray)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${activeDownloads.size} Active",
                            color = if (activeDownloads.isNotEmpty()) CyanNeon else TextMuted,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(16.dp)
                            .background(GlassBorderSubtle)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { viewModel.navigateTo(ScreenTab.HISTORY) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.DownloadDone,
                            contentDescription = "Completed",
                            tint = NeonViolet,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${completedDownloads.size} Downloaded",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(16.dp)
                            .background(GlassBorderSubtle)
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = "Engine Speed",
                            tint = HotMagenta,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Multi-Threaded",
                            color = HotMagenta,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
        }

        // Hero Search / URL Input Card
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 24.dp,
                backgroundColor = NavySurface,
                borderColor = GlassBorder
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Download Video or Audio",
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Auto-Detect Link",
                            color = CyanNeon,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Input Box
                    OutlinedTextField(
                        value = urlInput,
                        onValueChange = { viewModel.onUrlChanged(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("url_input_field"),
                        placeholder = {
                            Text(
                                text = "Paste video link from YouTube, TikTok, IG...",
                                color = TextMuted,
                                fontSize = 13.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Link,
                                contentDescription = "URL Link",
                                tint = CyanNeon
                            )
                        },
                        trailingIcon = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (urlInput.isNotEmpty()) {
                                    IconButton(onClick = { viewModel.onClearUrl() }) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "Clear",
                                            tint = TextMuted
                                        )
                                    }
                                }
                                IconButton(
                                    onClick = {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val clip = clipboard.primaryClip
                                        if (clip != null && clip.itemCount > 0) {
                                            val pasted = clip.getItemAt(0).text?.toString() ?: ""
                                            if (pasted.isNotBlank()) {
                                                viewModel.onUrlChanged(pasted)
                                            }
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentPaste,
                                        contentDescription = "Paste Clipboard",
                                        tint = CyanNeon
                                    )
                                }
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyanNeon,
                            unfocusedBorderColor = GlassBorderSubtle,
                            focusedContainerColor = NavySurfaceContainer,
                            unfocusedContainerColor = NavySurfaceContainer,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Uri,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                viewModel.analyzeUrl()
                            }
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Analyze Link Button
                    Button(
                        onClick = {
                            focusManager.clearFocus()
                            viewModel.analyzeUrl()
                        },
                        enabled = !isAnalyzing,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(PrimaryGradient)
                            .testTag("analyze_link_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            disabledContainerColor = NavySurfaceVariant
                        )
                    ) {
                        if (isAnalyzing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                color = Color.Black,
                                strokeWidth = 2.5.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Extracting Video Streams...",
                                color = Color.Black,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "Analyze",
                                    tint = Color.Black,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Analyze Link",
                                    color = Color.Black,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(22.dp))
        }

        // Supported Platforms Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Supported Platforms",
                    color = TextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Tap to Explore",
                    color = TextMuted,
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Platform Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                for (rowItems in platforms.chunked(2)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        for (platform in rowItems) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(NavySurfaceContainer)
                                    .border(1.dp, GlassBorderSubtle, RoundedCornerShape(18.dp))
                                    .clickable { viewModel.selectPlatform(platform.name) }
                                    .padding(14.dp)
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(platform.iconColor.copy(alpha = 0.2f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = platform.name.take(2).uppercase(),
                                                color = platform.iconColor,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Black
                                            )
                                        }

                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(NavySurfaceElevated)
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = platform.badge,
                                                color = CyanNeon,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Text(
                                        text = "${platform.name} Downloader",
                                        color = TextPrimary,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = platform.subtitle,
                                        color = TextMuted,
                                        fontSize = 11.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Featured Videos / Quick Download Showcase
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Trending Media",
                    color = TextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Instant 1-Click",
                    color = CyanNeon,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        items(featuredVideos) { video ->
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                cornerRadius = 16.dp,
                backgroundColor = NavySurfaceContainer,
                borderColor = GlassBorderSubtle
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = 90.dp, height = 66.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(NavyDarkBackground)
                    ) {
                        AsyncImage(
                            model = video.thumbnail,
                            contentDescription = "Video Thumbnail",
                            modifier = Modifier.matchParentSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(4.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.Black.copy(alpha = 0.8f))
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = video.duration,
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = video.title,
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            PlatformChip(platform = video.platform)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = video.author,
                                color = TextMuted,
                                fontSize = 11.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = { viewModel.analyzeUrl(video.url) },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(CyanNeon.copy(alpha = 0.15f))
                            .border(1.dp, CyanNeon.copy(alpha = 0.4f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Download Video",
                            tint = CyanNeon,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
