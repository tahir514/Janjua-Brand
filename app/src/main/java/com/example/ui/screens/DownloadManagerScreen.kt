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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.DownloadItem
import com.example.ui.components.GlassCard
import com.example.ui.components.GlowingProgressBar
import com.example.ui.components.PlatformChip
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.GlassBorderSubtle
import com.example.ui.theme.HotMagenta
import com.example.ui.theme.NavyDarkBackground
import com.example.ui.theme.NavySurface
import com.example.ui.theme.NavySurfaceContainer
import com.example.ui.theme.NavySurfaceElevated
import com.example.ui.theme.NavySurfaceVariant
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VividPurple
import com.example.ui.viewmodel.MainViewModel
import java.util.Locale

@Composable
fun DownloadManagerScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val activeDownloads by viewModel.activeDownloads.collectAsState()
    val completedDownloads by viewModel.completedDownloads.collectAsState()
    val subTab by viewModel.downloadsSubTab.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NavyDarkBackground)
    ) {
        // Screen Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Download Manager",
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "${activeDownloads.size} active • ${completedDownloads.size} completed",
                    color = TextMuted,
                    fontSize = 12.sp
                )
            }

            // Quick Batch Controls
            if (subTab == 0 && activeDownloads.isNotEmpty()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(NavySurfaceVariant)
                            .clickable { viewModel.pauseAll() }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Pause All",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(HotMagenta.copy(alpha = 0.15f))
                            .clickable { viewModel.cancelAll() }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Cancel All",
                            color = HotMagenta,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            } else if (subTab == 1 && completedDownloads.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(NavySurfaceVariant)
                        .clickable { viewModel.clearCompletedHistory() }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Clear All",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Segmented Tabs
        TabRow(
            selectedTabIndex = subTab,
            containerColor = NavySurfaceContainer,
            contentColor = CyanNeon,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[subTab]),
                    color = CyanNeon,
                    height = 3.dp
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(12.dp))
                .height(44.dp)
        ) {
            Tab(
                selected = subTab == 0,
                onClick = { viewModel.setDownloadsSubTab(0) },
                text = {
                    Text(
                        text = "Downloading (${activeDownloads.size})",
                        fontWeight = if (subTab == 0) FontWeight.Bold else FontWeight.Normal,
                        color = if (subTab == 0) CyanNeon else TextSecondary,
                        fontSize = 13.sp
                    )
                }
            )
            Tab(
                selected = subTab == 1,
                onClick = { viewModel.setDownloadsSubTab(1) },
                text = {
                    Text(
                        text = "Completed (${completedDownloads.size})",
                        fontWeight = if (subTab == 1) FontWeight.Bold else FontWeight.Normal,
                        color = if (subTab == 1) CyanNeon else TextSecondary,
                        fontSize = 13.sp
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (subTab == 0) {
            // Active Downloads List
            if (activeDownloads.isEmpty()) {
                EmptyStateCard(
                    icon = Icons.Default.CloudDownload,
                    title = "No Active Downloads",
                    subtitle = "Paste a link or pick a trending video on Home to start multi-threaded downloading."
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(activeDownloads, key = { it.id }) { item ->
                        ActiveDownloadCard(
                            item = item,
                            onPause = { viewModel.pauseDownload(item.id) },
                            onResume = { viewModel.resumeDownload(item.id) },
                            onCancel = { viewModel.cancelDownload(item.id) },
                            onRetry = { viewModel.retryDownload(item.id) }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(30.dp))
                    }
                }
            }
        } else {
            // Completed Downloads List
            if (completedDownloads.isEmpty()) {
                EmptyStateCard(
                    icon = Icons.Default.DownloadDone,
                    title = "No Completed Downloads Yet",
                    subtitle = "Completed media files will appear here ready to play, open or share."
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(completedDownloads, key = { it.id }) { item ->
                        CompletedDownloadItemCard(
                            item = item,
                            onPlay = { viewModel.setPreviewVideo(item) },
                            onDelete = { viewModel.deleteHistoryItem(item) }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(30.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ActiveDownloadCard(
    item: DownloadItem,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onCancel: () -> Unit,
    onRetry: () -> Unit
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 18.dp,
        backgroundColor = NavySurfaceContainer,
        borderColor = if (item.status == "DOWNLOADING") CyanNeon.copy(alpha = 0.4f) else GlassBorderSubtle
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Row with Thumbnail, Title & Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 80.dp, height = 60.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(NavyDarkBackground)
                ) {
                    AsyncImage(
                        model = item.thumbnailUrl,
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
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = item.duration,
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.title,
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        PlatformChip(platform = item.platform)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = item.quality,
                            color = NeonViolet,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Action button: Pause / Resume / Retry
                when (item.status) {
                    "DOWNLOADING" -> {
                        IconButton(
                            onClick = onPause,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(NavySurfaceVariant)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Pause,
                                contentDescription = "Pause",
                                tint = CyanNeon,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    "PAUSED" -> {
                        IconButton(
                            onClick = onResume,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(CyanNeon.copy(alpha = 0.15f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Resume",
                                tint = CyanNeon,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    "FAILED" -> {
                        IconButton(
                            onClick = onRetry,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(HotMagenta.copy(alpha = 0.15f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Replay,
                                contentDescription = "Retry",
                                tint = HotMagenta,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    else -> {
                        IconButton(
                            onClick = onPause,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(NavySurfaceVariant)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Pause,
                                contentDescription = "Pause",
                                tint = TextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                IconButton(
                    onClick = onCancel,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(NavySurfaceVariant)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cancel",
                        tint = TextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress Bar
            GlowingProgressBar(progress = item.progress, height = 7.dp)

            Spacer(modifier = Modifier.height(8.dp))

            // Stats: Speed, Percentage, ETA, Size
            val downloadedMb = item.downloadedBytes / (1024.0 * 1024.0)
            val totalMb = item.totalBytes / (1024.0 * 1024.0)
            val percentText = String.format(Locale.US, "%.1f%%", item.progress)

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = when (item.status) {
                        "DOWNLOADING" -> "${item.speed} • $percentText"
                        "PAUSED" -> "Paused ($percentText)"
                        "FAILED" -> "Failed • Tap retry"
                        else -> "Queued..."
                    },
                    color = when (item.status) {
                        "DOWNLOADING" -> CyanNeon
                        "PAUSED" -> TextSecondary
                        "FAILED" -> HotMagenta
                        else -> TextMuted
                    },
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "${String.format(Locale.US, "%.1f", downloadedMb)} MB / ${String.format(Locale.US, "%.1f", totalMb)} MB",
                    color = TextMuted,
                    fontSize = 11.sp
                )

                Text(
                    text = "ETA: ${item.eta}",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun CompletedDownloadItemCard(
    item: DownloadItem,
    onPlay: () -> Unit,
    onDelete: () -> Unit
) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPlay() },
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
                    .size(width = 80.dp, height = 58.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(NavyDarkBackground)
            ) {
                AsyncImage(
                    model = item.thumbnailUrl,
                    contentDescription = "Thumbnail",
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.7f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            tint = CyanNeon,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(3.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    PlatformChip(platform = item.platform)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = item.quality,
                        color = NeonViolet,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    val totalMb = item.totalBytes / (1024.0 * 1024.0)
                    Text(
                        text = "• ${String.format(Locale.US, "%.1f MB", totalMb)}",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            IconButton(
                onClick = onDelete,
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(NavySurfaceVariant)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Delete",
                    tint = TextMuted,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun EmptyStateCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(CyanNeon.copy(alpha = 0.1f))
                    .border(1.dp, CyanNeon.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "Empty",
                    tint = CyanNeon,
                    modifier = Modifier.size(36.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                color = TextPrimary,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = subtitle,
                color = TextMuted,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}
