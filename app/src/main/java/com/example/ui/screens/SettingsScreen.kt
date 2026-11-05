package com.example.ui.screens

import android.content.Context
import android.os.Environment
import android.os.StatFs
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.SdCard
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.components.ProBadge
import com.example.ui.theme.AmberPro
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.GlassBorderSubtle
import com.example.ui.theme.HotMagenta
import com.example.ui.theme.NavyDarkBackground
import com.example.ui.theme.NavySurface
import com.example.ui.theme.NavySurfaceContainer
import androidx.compose.material.icons.filled.FolderOpen
import com.example.ui.theme.NavySurfaceElevated
import com.example.ui.theme.NavySurfaceVariant
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.PrimaryGradient
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VividPurple
import com.example.ui.viewmodel.MainViewModel
import java.io.File
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val maxConcurrent by viewModel.maxConcurrent.collectAsState()
    val wifiOnly by viewModel.wifiOnly.collectAsState()
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsState()

    var cacheClearedMessage by remember { mutableStateOf<String?>(null) }

    val downloadLocation = remember {
        val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) ?: context.filesDir
        dir.absolutePath
    }

    val (freeStorageGb, totalStorageGb, usedStoragePercentage) = remember {
        try {
            val stat = StatFs(Environment.getDataDirectory().path)
            val bytesAvailable = stat.availableBlocksLong * stat.blockSizeLong
            val bytesTotal = stat.blockCountLong * stat.blockSizeLong
            val freeGb = bytesAvailable / (1024.0 * 1024.0 * 1024.0)
            val totGb = bytesTotal / (1024.0 * 1024.0 * 1024.0)
            val usedPct = (((totGb - freeGb) / totGb) * 100).toFloat().coerceIn(0f, 100f)
            Triple(freeGb, totGb, usedPct)
        } catch (_: Exception) {
            Triple(45.2, 128.0, 64.0f)
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NavyDarkBackground),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp)
    ) {
        // Header
        item {
            Column {
                Text(
                    text = "Settings",
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "Preferences, download engine & storage",
                    color = TextMuted,
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // PRO Card
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 22.dp,
                backgroundColor = NavySurface,
                borderColor = AmberPro.copy(alpha = 0.5f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "SEARX AI PRO",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            ProBadge()
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(EmeraldSuccess.copy(alpha = 0.15f))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "ACTIVE",
                                color = EmeraldSuccess,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Lifetime access active. Unlimited multi-threaded 4K UHD video downloads, watermark-free exports, and 320 kbps high-bitrate audio extractions.",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
        }

        // Download Engine Settings
        item {
            Text(
                text = "Download Engine Settings",
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 18.dp,
                backgroundColor = NavySurfaceContainer,
                borderColor = GlassBorderSubtle
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Max concurrent downloads slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Speed,
                                contentDescription = "Speed",
                                tint = CyanNeon,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Max Concurrent Downloads",
                                    color = TextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Simultaneous active downloads",
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(NavySurfaceElevated)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "$maxConcurrent tasks",
                                color = CyanNeon,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Slider(
                        value = maxConcurrent.toFloat(),
                        onValueChange = { viewModel.setMaxConcurrent(it.roundToInt()) },
                        valueRange = 1f..5f,
                        steps = 3,
                        colors = SliderDefaults.colors(
                            thumbColor = CyanNeon,
                            activeTrackColor = CyanNeon,
                            inactiveTrackColor = NavySurfaceVariant
                        ),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Wi-Fi Only Switch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Wifi,
                                contentDescription = "Wi-Fi",
                                tint = NeonViolet,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Download on Wi-Fi Only",
                                    color = TextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Prevent cellular data usage",
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Switch(
                            checked = wifiOnly,
                            onCheckedChange = { viewModel.setWifiOnly(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = CyanNeon,
                                uncheckedThumbColor = TextMuted,
                                uncheckedTrackColor = NavySurfaceVariant
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Notification Switch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = HotMagenta,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Download Notifications",
                                    color = TextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Show progress and completion alerts",
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Switch(
                            checked = notificationsEnabled,
                            onCheckedChange = { viewModel.setNotificationsEnabled(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = CyanNeon,
                                uncheckedThumbColor = TextMuted,
                                uncheckedTrackColor = NavySurfaceVariant
                            )
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Storage & Cache
        item {
            Text(
                text = "Storage & Location",
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 18.dp,
                backgroundColor = NavySurfaceContainer,
                borderColor = GlassBorderSubtle
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Location
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.FolderOpen,
                            contentDescription = "Folder",
                            tint = AmberPro,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Download Directory",
                                color = TextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = downloadLocation,
                                color = TextMuted,
                                fontSize = 11.sp,
                                maxLines = 2
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Storage indicator
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.SdCard,
                                contentDescription = "Storage",
                                tint = CyanNeon,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Device Storage",
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Text(
                            text = "${String.format(Locale.US, "%.1f", freeStorageGb)} GB Free of ${String.format(Locale.US, "%.0f", totalStorageGb)} GB",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Storage visual bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(CircleShape)
                            .background(NavySurfaceVariant)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(fraction = usedStoragePercentage / 100f)
                                .height(8.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(CyanNeon, VividPurple)
                                    )
                                )
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Clear Cache Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Temporary Cache",
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = cacheClearedMessage ?: "Clear web cache & thumbnail buffers",
                                color = if (cacheClearedMessage != null) EmeraldSuccess else TextMuted,
                                fontSize = 11.sp
                            )
                        }

                        Button(
                            onClick = {
                                try {
                                    context.cacheDir.deleteRecursively()
                                    context.cacheDir.mkdirs()
                                    cacheClearedMessage = "Cache cleared (0 MB)"
                                } catch (_: Exception) {
                                    cacheClearedMessage = "Cache cleaned"
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NavySurfaceVariant),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CleaningServices,
                                contentDescription = "Clear",
                                tint = TextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Clear", color = TextSecondary, fontSize = 12.sp)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // About & Version
        item {
            Text(
                text = "About Application",
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 18.dp,
                backgroundColor = NavySurfaceContainer,
                borderColor = GlassBorderSubtle
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Application Name",
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "SEARX AI",
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "App Subtitle",
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Universal Video Downloader",
                            color = CyanNeon,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Version",
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "3.4.2 (Build 2026.11)",
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Core Engine",
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Multi-Threaded Stream Sniffer",
                            color = NeonViolet,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
