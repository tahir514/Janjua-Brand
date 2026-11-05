package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "download_items")
data class DownloadItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val sourceUrl: String,
    val thumbnailUrl: String,
    val platform: String, // YouTube, TikTok, Instagram, Facebook, Twitter, Web
    val quality: String, // 1080p Full HD, 720p HD, 4K Ultra HD, MP3 Audio, etc.
    val formatType: String, // VIDEO, AUDIO
    val fileExtension: String, // mp4, mp3, m4a
    val filePath: String,
    val totalBytes: Long,
    val downloadedBytes: Long,
    val progress: Float, // 0.0 to 100.0
    val speed: String, // e.g., "4.8 MB/s"
    val eta: String, // e.g., "00:15"
    val status: String, // QUEUED, DOWNLOADING, PAUSED, COMPLETED, FAILED, CANCELLED
    val duration: String, // e.g., "03:42"
    val author: String, // e.g., "@travel_world"
    val createdAt: Long = System.currentTimeMillis()
) {
    val isCompleted: Boolean get() = status == "COMPLETED"
    val isActive: Boolean get() = status == "DOWNLOADING" || status == "QUEUED" || status == "PAUSED"
    val isPaused: Boolean get() = status == "PAUSED"
    val isFailed: Boolean get() = status == "FAILED"
}
