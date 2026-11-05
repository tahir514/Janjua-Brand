package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.example.data.model.DownloadItem
import com.example.ui.components.GlassCard
import com.example.ui.components.PlatformChip
import com.example.ui.theme.CyanNeon
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
import com.example.ui.viewmodel.MainViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val completedDownloads by viewModel.completedDownloads.collectAsState()
    val activeFilter by viewModel.historyFilter.collectAsState()
    val searchQuery by viewModel.historySearchQuery.collectAsState()

    var showClearConfirm by remember { mutableStateOf(false) }

    val filterCategories = listOf("All", "Videos", "Audio", "YouTube", "TikTok", "Instagram", "Facebook")

    val filteredList = completedDownloads.filter { item ->
        val matchesSearch = item.title.contains(searchQuery, ignoreCase = true) ||
                item.author.contains(searchQuery, ignoreCase = true)
        val matchesCategory = when (activeFilter) {
            "All" -> true
            "Videos" -> item.formatType == "VIDEO"
            "Audio" -> item.formatType == "AUDIO"
            else -> item.platform.equals(activeFilter, ignoreCase = true)
        }
        matchesSearch && matchesCategory
    }

    if (showClearConfirm) {
        BasicAlertDialog(onDismissRequest = { showClearConfirm = false }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(NavySurface)
                    .padding(20.dp)
            ) {
                Text(
                    text = "Clear All Download History?",
                    color = TextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "This will remove all completed download records from your local history.",
                    color = TextMuted,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(18.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = { showClearConfirm = false },
                        colors = ButtonDefaults.buttonColors(containerColor = NavySurfaceVariant)
                    ) {
                        Text("Cancel", color = TextSecondary)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = {
                            viewModel.clearCompletedHistory()
                            showClearConfirm = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = HotMagenta)
                    ) {
                        Text("Clear History", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NavyDarkBackground)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Download History",
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "${completedDownloads.size} items saved permanently",
                    color = TextMuted,
                    fontSize = 12.sp
                )
            }

            if (completedDownloads.isNotEmpty()) {
                IconButton(
                    onClick = { showClearConfirm = true },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(NavySurfaceVariant)
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteSweep,
                        contentDescription = "Clear History",
                        tint = HotMagenta,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.setHistorySearchQuery(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            placeholder = {
                Text(
                    text = "Search downloaded media...",
                    color = TextMuted,
                    fontSize = 13.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = CyanNeon
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.setHistorySearchQuery("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear Search",
                            tint = TextMuted
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
            shape = RoundedCornerShape(14.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Filter Chips
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filterCategories) { cat ->
                val isSelected = activeFilter == cat
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) CyanNeon else NavySurfaceContainer)
                        .clickable { viewModel.setHistoryFilter(cat) }
                        .padding(horizontal = 14.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = cat,
                        color = if (isSelected) Color.Black else TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (filteredList.isEmpty()) {
            EmptyStateCard(
                icon = Icons.Default.History,
                title = if (completedDownloads.isEmpty()) "No Download History" else "No Matching Media",
                subtitle = if (completedDownloads.isEmpty())
                    "Videos and audio tracks you complete downloading will stay saved here permanently."
                else "Try adjusting your search terms or filter."
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredList, key = { it.id }) { item ->
                    HistoryItemCard(
                        item = item,
                        onPlay = { viewModel.setPreviewVideo(item) },
                        onOpen = { openFileWithExternalApp(context, item) },
                        onShare = { shareMediaFile(context, item) },
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

@Composable
fun HistoryItemCard(
    item: DownloadItem,
    onPlay: () -> Unit,
    onOpen: () -> Unit,
    onShare: () -> Unit,
    onDelete: () -> Unit
) {
    val dateFormatted = remember(item.createdAt) {
        val sdf = SimpleDateFormat("MMM dd, yyyy • HH:mm", Locale.getDefault())
        sdf.format(Date(item.createdAt))
    }

    val sizeMb = item.totalBytes / (1024.0 * 1024.0)
    val sizeText = if (sizeMb >= 1024) String.format(Locale.US, "%.2f GB", sizeMb / 1024.0)
    else String.format(Locale.US, "%.1f MB", sizeMb)

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 18.dp,
        backgroundColor = NavySurfaceContainer,
        borderColor = GlassBorderSubtle
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 86.dp, height = 64.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(NavyDarkBackground)
                        .clickable { onPlay() }
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
                            .background(Color.Black.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(CyanNeon),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play",
                                tint = Color.Black,
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
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
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
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "$sizeText • $dateFormatted",
                        color = TextMuted,
                        fontSize = 10.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons Row: Play, Open, Share, Delete
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(NavySurfaceVariant)
                        .clickable { onPlay() }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            tint = CyanNeon,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Play", color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(NavySurfaceVariant)
                        .clickable { onOpen() }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.OpenInNew,
                            contentDescription = "Open",
                            tint = TextSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Open", color = TextSecondary, fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(NavySurfaceVariant)
                        .clickable { onShare() }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = TextSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Share", color = TextSecondary, fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(NavySurfaceVariant)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = TextMuted,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
        }
    }
}

fun openFileWithExternalApp(context: Context, item: DownloadItem) {
    try {
        val file = if (item.filePath.isNotEmpty()) File(item.filePath) else null
        val uri: Uri = if (file != null && file.exists()) {
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
        } else {
            Uri.parse(item.sourceUrl)
        }

        val mimeType = if (item.formatType == "AUDIO") "audio/*" else "video/*"
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, mimeType)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(Intent.createChooser(intent, "Open with"))
    } catch (_: Exception) {
    }
}

fun shareMediaFile(context: Context, item: DownloadItem) {
    try {
        val file = if (item.filePath.isNotEmpty()) File(item.filePath) else null
        val intent = Intent(Intent.ACTION_SEND).apply {
            if (file != null && file.exists()) {
                val uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    file
                )
                putExtra(Intent.EXTRA_STREAM, uri)
                setDataAndType(uri, if (item.formatType == "AUDIO") "audio/*" else "video/*")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            } else {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, item.title)
                putExtra(Intent.EXTRA_TEXT, "Watch '${item.title}': ${item.sourceUrl}")
            }
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(Intent.createChooser(intent, "Share media"))
    } catch (_: Exception) {
    }
}
