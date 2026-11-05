package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.engine.AnalyzedMediaInfo
import com.example.engine.MediaFormatOption
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.GlassBorderSubtle
import com.example.ui.theme.GlassFill
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
import com.example.ui.theme.VividPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormatSelectorSheet(
    mediaInfo: AnalyzedMediaInfo,
    selectedFormat: MediaFormatOption?,
    removeWatermark: Boolean,
    onFormatSelected: (MediaFormatOption) -> Unit,
    onToggleWatermark: (Boolean) -> Unit,
    onConfirmDownload: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Video, 1: Audio

    val videoFormats = mediaInfo.formats.filter { it.formatType == "VIDEO" }
    val audioFormats = mediaInfo.formats.filter { it.formatType == "AUDIO" }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = NavySurface,
        scrimColor = Color.Black.copy(alpha = 0.75f),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp)
        ) {
            // Header: Title and Close
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(CyanNeon.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.HighQuality,
                            contentDescription = "Quality",
                            tint = CyanNeon,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Download Options",
                            color = TextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Select quality & resolution",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(NavySurfaceVariant)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Media Preview Card
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
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
                            .size(width = 84.dp, height = 64.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(NavyDarkBackground)
                    ) {
                        AsyncImage(
                            model = mediaInfo.thumbnailUrl,
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
                                text = mediaInfo.duration,
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = mediaInfo.title,
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            PlatformChip(platform = mediaInfo.platform)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = mediaInfo.author,
                                color = TextMuted,
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Tabs: Video / Audio
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = NavySurfaceContainer,
                contentColor = CyanNeon,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = CyanNeon,
                        height = 3.dp
                    )
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .height(44.dp)
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Videocam,
                                contentDescription = "Video",
                                modifier = Modifier.size(16.dp),
                                tint = if (selectedTab == 0) CyanNeon else TextSecondary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Video Formats",
                                fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == 0) CyanNeon else TextSecondary,
                                fontSize = 13.sp
                            )
                        }
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Headphones,
                                contentDescription = "Audio",
                                modifier = Modifier.size(16.dp),
                                tint = if (selectedTab == 1) CyanNeon else TextSecondary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Audio (MP3/M4A)",
                                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == 1) CyanNeon else TextSecondary,
                                fontSize = 13.sp
                            )
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Watermark Toggle if supported
            if (mediaInfo.canRemoveWatermark) {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    cornerRadius = 12.dp,
                    backgroundColor = NavySurfaceVariant.copy(alpha = 0.5f),
                    borderColor = GlassBorderSubtle
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Authorized No-Watermark Export",
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Export clean video stream when authorized",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }
                        Switch(
                            checked = removeWatermark,
                            onCheckedChange = onToggleWatermark,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = CyanNeon,
                                uncheckedThumbColor = TextMuted,
                                uncheckedTrackColor = NavySurfaceContainer
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Quality List
            val displayFormats = if (selectedTab == 0) videoFormats else audioFormats

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(displayFormats, key = { it.id }) { format ->
                    val isSelected = selectedFormat?.id == format.id
                    val itemBg = if (isSelected) NavySurfaceElevated else NavySurfaceContainer
                    val borderCol = if (isSelected) CyanNeon else GlassBorderSubtle

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(itemBg)
                            .border(if (isSelected) 1.5.dp else 0.8.dp, borderCol, RoundedCornerShape(12.dp))
                            .clickable { onFormatSelected(format) }
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) CyanNeon else NavySurfaceVariant)
                                        .border(
                                            1.dp,
                                            if (isSelected) CyanNeon else TextMuted,
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Selected",
                                            tint = Color.Black,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = format.resolutionLabel,
                                            color = if (isSelected) CyanNeon else TextPrimary,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(
                                                    if (format.qualityTag.contains("4K") || format.qualityTag.contains("Ultra") || format.qualityTag.contains("Recommended"))
                                                        VividPurple.copy(alpha = 0.25f)
                                                    else NavySurfaceVariant
                                                )
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = format.qualityTag,
                                                color = if (format.qualityTag.contains("4K") || format.qualityTag.contains("Recommended"))
                                                    NeonViolet
                                                else TextSecondary,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                    Text(
                                        text = "${format.extension.uppercase()} format",
                                        color = TextMuted,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            Text(
                                text = format.estimatedSizeFormatted,
                                color = if (isSelected) TextPrimary else TextSecondary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Start Download Button
            Button(
                onClick = onConfirmDownload,
                enabled = selectedFormat != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(PrimaryGradient)
                    .testTag("confirm_download_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    disabledContainerColor = NavySurfaceVariant
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = "Start Download",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Start Download (${selectedFormat?.resolutionLabel ?: ""})",
                        color = Color.Black,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
    }
}
