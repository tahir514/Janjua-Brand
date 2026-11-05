package com.example

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.CloudDownload
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.data.model.DownloadItem
import com.example.ui.components.FormatSelectorSheet
import com.example.ui.components.VideoPlayerDialog
import com.example.ui.screens.DownloadManagerScreen
import com.example.ui.screens.ExploreScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.shareMediaFile
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.GlassBorderSubtle
import com.example.ui.theme.GlassFill
import com.example.ui.theme.NavyDarkBackground
import com.example.ui.theme.NavySurface
import com.example.ui.theme.NavySurfaceElevated
import com.example.ui.theme.NavySurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VividPurple
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.ScreenTab
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Handle incoming shared links from other apps (e.g. YouTube "Share to SEARX AI")
        handleSharedIntent(intent)

        setContent {
            MyApplicationTheme {
                MainAppContent(viewModel = viewModel)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleSharedIntent(intent)
    }

    private fun handleSharedIntent(intent: Intent?) {
        if (intent == null) return
        if (Intent.ACTION_SEND == intent.action && intent.type == "text/plain") {
            val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
            if (!sharedText.isNullOrBlank()) {
                viewModel.onUrlChanged(sharedText)
                viewModel.analyzeUrl(sharedText)
            }
        }
    }
}

data class NavItem(
    val tab: ScreenTab,
    val title: String,
    val activeIcon: ImageVector,
    val inactiveIcon: ImageVector,
    val testTag: String
)

@Composable
fun MainAppContent(viewModel: MainViewModel) {
    val context = LocalContext.current
    val currentTab by viewModel.currentScreen.collectAsState()
    val activeDownloads by viewModel.activeDownloads.collectAsState()
    val showFormatSheet by viewModel.showFormatSheet.collectAsState()
    val analyzedMedia by viewModel.analyzedMedia.collectAsState()
    val selectedFormat by viewModel.selectedFormat.collectAsState()
    val removeWatermark by viewModel.removeWatermark.collectAsState()
    val previewVideoItem by viewModel.previewVideoItem.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Request notification permission for Android 13+
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { _ -> }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val isGranted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!isGranted) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    // Handle toast/snackbar events
    LaunchedEffect(Unit) {
        viewModel.toastEvent.collectLatest { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    // Android back navigation: pop to HOME if on other tabs
    BackHandler(enabled = currentTab != ScreenTab.HOME) {
        viewModel.navigateTo(ScreenTab.HOME)
    }

    val navItems = listOf(
        NavItem(ScreenTab.HOME, "Home", Icons.Filled.Home, Icons.Outlined.Home, "nav_home"),
        NavItem(ScreenTab.DOWNLOADS, "Downloads", Icons.Filled.CloudDownload, Icons.Outlined.CloudDownload, "nav_downloads"),
        NavItem(ScreenTab.EXPLORE, "Explore", Icons.Filled.Language, Icons.Outlined.Language, "nav_explore"),
        NavItem(ScreenTab.HISTORY, "History", Icons.Filled.History, Icons.Outlined.History, "nav_history"),
        NavItem(ScreenTab.SETTINGS, "Settings", Icons.Filled.Settings, Icons.Outlined.Settings, "nav_settings")
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
        containerColor = NavyDarkBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .background(NavySurface)
                    .border(0.8.dp, GlassBorderSubtle, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .padding(vertical = 8.dp, horizontal = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    navItems.forEach { item ->
                        val isSelected = currentTab == item.tab
                        val activeColor = CyanNeon
                        val inactiveColor = TextMuted

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { viewModel.navigateTo(item.tab) }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .testTag(item.testTag)
                        ) {
                            Box(contentAlignment = Alignment.TopEnd) {
                                Icon(
                                    imageVector = if (isSelected) item.activeIcon else item.inactiveIcon,
                                    contentDescription = item.title,
                                    tint = if (isSelected) activeColor else inactiveColor,
                                    modifier = Modifier.size(24.dp)
                                )

                                // Active download counter badge on Downloads tab
                                if (item.tab == ScreenTab.DOWNLOADS && activeDownloads.isNotEmpty()) {
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clip(CircleShape)
                                            .background(CyanNeon),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${activeDownloads.size}",
                                            color = Color.Black,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Black
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = item.title,
                                color = if (isSelected) activeColor else inactiveColor,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Crossfade(targetState = currentTab, label = "tab_fade") { tab ->
                when (tab) {
                    ScreenTab.HOME -> HomeScreen(viewModel = viewModel)
                    ScreenTab.DOWNLOADS -> DownloadManagerScreen(viewModel = viewModel)
                    ScreenTab.EXPLORE -> ExploreScreen(viewModel = viewModel)
                    ScreenTab.HISTORY -> HistoryScreen(viewModel = viewModel)
                    ScreenTab.SETTINGS -> SettingsScreen(viewModel = viewModel)
                }
            }

            // Quality Selection Modal Bottom Sheet
            if (showFormatSheet && analyzedMedia != null) {
                FormatSelectorSheet(
                    mediaInfo = analyzedMedia!!,
                    selectedFormat = selectedFormat,
                    removeWatermark = removeWatermark,
                    onFormatSelected = { viewModel.selectFormat(it) },
                    onToggleWatermark = { viewModel.toggleWatermark(it) },
                    onConfirmDownload = {
                        viewModel.startDownload()
                        viewModel.navigateTo(ScreenTab.DOWNLOADS)
                    },
                    onDismiss = { viewModel.dismissFormatSheet() }
                )
            }

            // In-App Video Player Dialog
            previewVideoItem?.let { item ->
                VideoPlayerDialog(
                    item = item,
                    onDismiss = { viewModel.setPreviewVideo(null) },
                    onShare = { shareMediaFile(context, it) }
                )
            }
        }
    }
}
