package com.example.ui.screens

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.components.GlassCard
import com.example.ui.components.PlatformChip
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.FacebookBlue
import com.example.ui.theme.GlassBorderSubtle
import com.example.ui.theme.HotMagenta
import com.example.ui.theme.InstagramPurple
import com.example.ui.theme.NavyDarkBackground
import com.example.ui.theme.NavySurface
import com.example.ui.theme.NavySurfaceContainer
import com.example.ui.theme.NavySurfaceElevated
import com.example.ui.theme.NavySurfaceVariant
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TikTokCyan
import com.example.ui.theme.TwitterSlate
import com.example.ui.theme.YouTubeRed
import com.example.ui.viewmodel.MainViewModel

data class SiteBookmark(
    val name: String,
    val initial: String,
    val color: Color,
    val defaultUrl: String
)

@Composable
fun ExploreScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val bookmarks = listOf(
        SiteBookmark("YouTube", "YT", YouTubeRed, "https://www.youtube.com/watch?v=Cyberpunk2077_HDR"),
        SiteBookmark("TikTok", "TT", TikTokCyan, "https://www.tiktok.com/@neon_explorer/video/7289382103"),
        SiteBookmark("Instagram", "IG", InstagramPurple, "https://www.instagram.com/reel/C892_VFX_Art"),
        SiteBookmark("Facebook", "FB", FacebookBlue, "https://www.facebook.com/watch/?v=9823412098"),
        SiteBookmark("X / Twitter", "X", TwitterSlate, "https://twitter.com/NextGenRobotics/status/178293"),
        SiteBookmark("Vimeo", "VM", Color(0xFF1AB7EA), "https://vimeo.com/89123891"),
        SiteBookmark("Reddit", "RD", Color(0xFFFF4500), "https://reddit.com/r/videos"),
        SiteBookmark("Dailymotion", "DM", Color(0xFF0066DC), "https://dailymotion.com/video")
    )

    val viralReels = listOf(
        FeaturedVideoData(
            title = "Unreal Engine 5.5 Next-Gen Dynamic Physics & Lumen Demo",
            author = "Epic Tech Community",
            platform = "YouTube",
            duration = "05:12",
            thumbnail = "https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=800&q=80",
            url = "https://www.youtube.com/watch?v=Unreal55_Lumen"
        ),
        FeaturedVideoData(
            title = "Cinematic Night Sky Aurora Borealis Timelapse 4K HDR",
            author = "@aurora_skies",
            platform = "TikTok",
            duration = "00:58",
            thumbnail = "https://images.unsplash.com/photo-1531366936337-7c912a4589a7?w=800&q=80",
            url = "https://www.tiktok.com/@aurora_skies/video/81923"
        ),
        FeaturedVideoData(
            title = "Cyber Ambient Chill Beat & Visualizer Studio Mix #Music",
            author = "Synthwave Pulse",
            platform = "Instagram",
            duration = "04:30",
            thumbnail = "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=800&q=80",
            url = "https://www.instagram.com/reel/SynthwavePulse"
        ),
        FeaturedVideoData(
            title = "Supercar Mountain Pass Full Throttle Acceleration 60FPS",
            author = "Apex Motorsports",
            platform = "Facebook",
            duration = "03:15",
            thumbnail = "https://images.unsplash.com/photo-1503376780353-7e6692767b70?w=800&q=80",
            url = "https://www.facebook.com/watch/?v=SupercarApex"
        )
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NavyDarkBackground),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Video Explorer",
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "Browse supported media platforms & extract streams",
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(CyanNeon.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = "Web",
                        tint = CyanNeon,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
        }

        // Quick Bookmarks Carousel
        item {
            Text(
                text = "Quick Platform Access",
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(bookmarks) { site ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable {
                            viewModel.onUrlChanged(site.defaultUrl)
                            viewModel.analyzeUrl(site.defaultUrl)
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(NavySurfaceContainer)
                                .border(1.dp, site.color.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = site.initial,
                                color = site.color,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = site.name,
                            color = TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // AI Detection Feature Banner
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 20.dp,
                backgroundColor = NavySurface,
                borderColor = CyanNeon.copy(alpha = 0.3f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(CyanNeon.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "AI Engine",
                            tint = CyanNeon,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "SEARX Smart Media Sniffer",
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Automatically uncovers maximum 4K bitrate video streams and 320k HQ audio tracks.",
                            color = TextMuted,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Trending Viral Streams
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.TrendingUp,
                    contentDescription = "Trending",
                    tint = HotMagenta,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Trending Viral Clips",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        items(viralReels) { video ->
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
                            .size(width = 88.dp, height = 64.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(NavyDarkBackground)
                    ) {
                        AsyncImage(
                            model = video.thumbnail,
                            contentDescription = "Thumbnail",
                            modifier = Modifier.matchParentSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(3.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.Black.copy(alpha = 0.8f))
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = video.duration,
                                color = Color.White,
                                fontSize = 9.sp,
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

                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(CyanNeon.copy(alpha = 0.15f))
                            .border(1.dp, CyanNeon.copy(alpha = 0.4f), CircleShape)
                            .clickable { viewModel.analyzeUrl(video.url) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Download",
                            tint = CyanNeon,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
