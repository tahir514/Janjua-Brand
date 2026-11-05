package com.example.data.repository

import android.content.Context
import com.example.data.db.AppDatabase
import com.example.data.model.DownloadItem
import com.example.engine.DownloadEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File

class DownloadRepository(private val context: Context) {

    private val db = AppDatabase.getInstance(context)
    private val dao = db.downloadDao()
    private val engine = DownloadEngine.getInstance(context)

    val allDownloads: Flow<List<DownloadItem>> = dao.getAllDownloads()
    val activeDownloads: Flow<List<DownloadItem>> = dao.getActiveDownloads()
    val completedDownloads: Flow<List<DownloadItem>> = dao.getCompletedDownloads()

    init {
        // Pre-populate with realistic starter records if table is brand new
        CoroutineScope(Dispatchers.IO).launch {
            val current = dao.getAllDownloads().first()
            if (current.isEmpty()) {
                seedInitialData()
            }
        }
    }

    private suspend fun seedInitialData() {
        val sample1 = DownloadItem(
            title = "Cyberpunk 2077 Night City 4K Ultra Cinematic HDR",
            sourceUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            thumbnailUrl = "https://images.unsplash.com/photo-1542751371-adc38448a05e?w=800&q=80",
            platform = "YouTube",
            quality = "1080p Full HD",
            formatType = "VIDEO",
            fileExtension = "mp4",
            filePath = "",
            totalBytes = 47_185_920L,
            downloadedBytes = 47_185_920L,
            progress = 100f,
            speed = "0.0 KB/s",
            eta = "Done",
            status = "COMPLETED",
            duration = "08:42",
            author = "FutureTech Official",
            createdAt = System.currentTimeMillis() - 3600_000
        )

        val sample2 = DownloadItem(
            title = "Insane cinematic drone glide through futuristic Tokyo neon streets",
            sourceUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
            thumbnailUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=800&q=80",
            platform = "TikTok",
            quality = "720p HD",
            formatType = "VIDEO",
            fileExtension = "mp4",
            filePath = "",
            totalBytes = 23_592_960L,
            downloadedBytes = 23_592_960L,
            progress = 100f,
            speed = "0.0 KB/s",
            eta = "Done",
            status = "COMPLETED",
            duration = "00:45",
            author = "@neon_explorer",
            createdAt = System.currentTimeMillis() - 7200_000
        )

        val sample3 = DownloadItem(
            title = "Bioluminescent Deep Sea Creatures in 4K HDR",
            sourceUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            thumbnailUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=800&q=80",
            platform = "Facebook",
            quality = "1080p Full HD",
            formatType = "VIDEO",
            fileExtension = "mp4",
            filePath = "",
            totalBytes = 52_428_800L,
            downloadedBytes = 28_311_552L,
            progress = 54.0f,
            speed = "4.2 MB/s",
            eta = "00:06",
            status = "DOWNLOADING",
            duration = "12:30",
            author = "Earth Odyssey Watch",
            createdAt = System.currentTimeMillis()
        )

        dao.insertDownload(sample1)
        dao.insertDownload(sample2)
        dao.insertDownload(sample3)
    }

    fun enqueue(item: DownloadItem) {
        engine.enqueueDownload(item)
    }

    fun pause(id: Long) {
        engine.pauseDownload(id)
    }

    fun resume(id: Long) {
        engine.resumeDownload(id)
    }

    fun cancel(id: Long) {
        engine.cancelDownload(id)
    }

    fun retry(id: Long) {
        engine.retryDownload(id)
    }

    fun pauseAll() {
        engine.pauseAll()
    }

    fun resumeAll() {
        engine.resumeAll()
    }

    fun cancelAll() {
        engine.cancelAll()
    }

    suspend fun deleteHistoryItem(item: DownloadItem) {
        if (item.filePath.isNotEmpty()) {
            val file = File(item.filePath)
            if (file.exists()) file.delete()
        }
        dao.deleteById(item.id)
    }

    suspend fun clearCompleted() {
        dao.clearCompleted()
    }

    fun setMaxConcurrentDownloads(count: Int) {
        engine.maxConcurrentDownloads = count
    }
}
