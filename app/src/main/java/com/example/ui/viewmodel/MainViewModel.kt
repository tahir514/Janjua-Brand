package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.DownloadItem
import com.example.data.repository.DownloadRepository
import com.example.engine.AnalyzedMediaInfo
import com.example.engine.MediaFormatOption
import com.example.engine.VideoAnalyzer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ScreenTab {
    HOME,
    DOWNLOADS,
    EXPLORE,
    HISTORY,
    SETTINGS
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DownloadRepository(application)

    private val _currentScreen = MutableStateFlow(ScreenTab.HOME)
    val currentScreen: StateFlow<ScreenTab> = _currentScreen.asStateFlow()

    private val _urlInput = MutableStateFlow("")
    val urlInput: StateFlow<String> = _urlInput.asStateFlow()

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    private val _analyzedMedia = MutableStateFlow<AnalyzedMediaInfo?>(null)
    val analyzedMedia: StateFlow<AnalyzedMediaInfo?> = _analyzedMedia.asStateFlow()

    private val _showFormatSheet = MutableStateFlow(false)
    val showFormatSheet: StateFlow<Boolean> = _showFormatSheet.asStateFlow()

    private val _selectedFormat = MutableStateFlow<MediaFormatOption?>(null)
    val selectedFormat: StateFlow<MediaFormatOption?> = _selectedFormat.asStateFlow()

    private val _removeWatermark = MutableStateFlow(true)
    val removeWatermark: StateFlow<Boolean> = _removeWatermark.asStateFlow()

    private val _downloadsSubTab = MutableStateFlow(0) // 0: Downloading, 1: Completed
    val downloadsSubTab: StateFlow<Int> = _downloadsSubTab.asStateFlow()

    private val _historyFilter = MutableStateFlow("All")
    val historyFilter: StateFlow<String> = _historyFilter.asStateFlow()

    private val _historySearchQuery = MutableStateFlow("")
    val historySearchQuery: StateFlow<String> = _historySearchQuery.asStateFlow()

    private val _previewVideoItem = MutableStateFlow<DownloadItem?>(null)
    val previewVideoItem: StateFlow<DownloadItem?> = _previewVideoItem.asStateFlow()

    // Settings
    private val _maxConcurrent = MutableStateFlow(3)
    val maxConcurrent: StateFlow<Int> = _maxConcurrent.asStateFlow()

    private val _wifiOnly = MutableStateFlow(false)
    val wifiOnly: StateFlow<Boolean> = _wifiOnly.asStateFlow()

    private val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow()

    // Snackbar notifications
    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent.asSharedFlow()

    val activeDownloads: StateFlow<List<DownloadItem>> = repository.activeDownloads
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val completedDownloads: StateFlow<List<DownloadItem>> = repository.completedDownloads
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val allDownloads: StateFlow<List<DownloadItem>> = repository.allDownloads
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun navigateTo(tab: ScreenTab) {
        _currentScreen.value = tab
    }

    fun onUrlChanged(newUrl: String) {
        _urlInput.value = newUrl
    }

    fun onClearUrl() {
        _urlInput.value = ""
    }

    fun analyzeUrl(customUrl: String? = null) {
        val targetUrl = customUrl ?: _urlInput.value.trim()
        if (targetUrl.isBlank()) {
            emitToast("Please enter or paste a valid video URL")
            return
        }

        viewModelScope.launch {
            _isAnalyzing.value = true
            // Simulate high-tech neural network link parsing & format extraction
            delay(1000)
            val info = VideoAnalyzer.analyze(targetUrl)
            _analyzedMedia.value = info
            _selectedFormat.value = info.formats.firstOrNull { it.id == "fmt_1080p" }
                ?: info.formats.firstOrNull()
            _isAnalyzing.value = false
            _showFormatSheet.value = true
        }
    }

    fun selectPlatform(platformName: String) {
        val presetUrl = when (platformName) {
            "YouTube" -> "https://www.youtube.com/watch?v=Cyberpunk2077_HDR"
            "TikTok" -> "https://www.tiktok.com/@neon_explorer/video/7289382103"
            "Instagram" -> "https://www.instagram.com/reel/C892_VFX_Art"
            "Facebook" -> "https://www.facebook.com/watch/?v=9823412098"
            "Twitter" -> "https://twitter.com/NextGenRobotics/status/178293"
            else -> "https://vimeo.com/89123891"
        }
        _urlInput.value = presetUrl
        analyzeUrl(presetUrl)
    }

    fun selectFormat(format: MediaFormatOption) {
        _selectedFormat.value = format
    }

    fun toggleWatermark(value: Boolean) {
        _removeWatermark.value = value
    }

    fun dismissFormatSheet() {
        _showFormatSheet.value = false
    }

    fun startDownload() {
        val media = _analyzedMedia.value ?: return
        val format = _selectedFormat.value ?: return

        val titleSuffix = if (_removeWatermark.value && media.canRemoveWatermark) " (No Watermark)" else ""
        val item = DownloadItem(
            title = media.title + titleSuffix,
            sourceUrl = format.directUrl.ifEmpty { media.originalUrl },
            thumbnailUrl = media.thumbnailUrl,
            platform = media.platform,
            quality = format.resolutionLabel,
            formatType = format.formatType,
            fileExtension = format.extension,
            filePath = "",
            totalBytes = format.estimatedSizeBytes,
            downloadedBytes = 0L,
            progress = 0f,
            speed = "0.0 KB/s",
            eta = "--:--",
            status = "QUEUED",
            duration = media.duration,
            author = media.author,
            createdAt = System.currentTimeMillis()
        )

        repository.enqueue(item)
        _showFormatSheet.value = false
        _urlInput.value = ""
        emitToast("Added to download queue: ${format.resolutionLabel}")
        _downloadsSubTab.value = 0
    }

    fun pauseDownload(id: Long) = repository.pause(id)
    fun resumeDownload(id: Long) = repository.resume(id)
    fun cancelDownload(id: Long) = repository.cancel(id)
    fun retryDownload(id: Long) = repository.retry(id)

    fun pauseAll() {
        repository.pauseAll()
        emitToast("Paused all downloads")
    }

    fun resumeAll() {
        repository.resumeAll()
        emitToast("Resumed all downloads")
    }

    fun cancelAll() {
        repository.cancelAll()
        emitToast("Cancelled all active downloads")
    }

    fun deleteHistoryItem(item: DownloadItem) {
        viewModelScope.launch {
            repository.deleteHistoryItem(item)
            emitToast("Removed from history")
        }
    }

    fun clearCompletedHistory() {
        viewModelScope.launch {
            repository.clearCompleted()
            emitToast("Completed download history cleared")
        }
    }

    fun setDownloadsSubTab(tabIndex: Int) {
        _downloadsSubTab.value = tabIndex
    }

    fun setHistoryFilter(filter: String) {
        _historyFilter.value = filter
    }

    fun setHistorySearchQuery(query: String) {
        _historySearchQuery.value = query
    }

    fun setPreviewVideo(item: DownloadItem?) {
        _previewVideoItem.value = item
    }

    fun setMaxConcurrent(count: Int) {
        _maxConcurrent.value = count
        repository.setMaxConcurrentDownloads(count)
    }

    fun setWifiOnly(enabled: Boolean) {
        _wifiOnly.value = enabled
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        _notificationsEnabled.value = enabled
    }

    private fun emitToast(message: String) {
        viewModelScope.launch {
            _toastEvent.emit(message)
        }
    }
}
