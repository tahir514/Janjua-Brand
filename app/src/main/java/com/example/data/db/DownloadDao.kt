package com.example.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.DownloadItem
import kotlinx.coroutines.flow.Flow

@Dao
interface DownloadDao {
    @Query("SELECT * FROM download_items ORDER BY createdAt DESC")
    fun getAllDownloads(): Flow<List<DownloadItem>>

    @Query("SELECT * FROM download_items WHERE status IN ('QUEUED', 'DOWNLOADING', 'PAUSED') ORDER BY createdAt DESC")
    fun getActiveDownloads(): Flow<List<DownloadItem>>

    @Query("SELECT * FROM download_items WHERE status = 'COMPLETED' ORDER BY createdAt DESC")
    fun getCompletedDownloads(): Flow<List<DownloadItem>>

    @Query("SELECT * FROM download_items WHERE id = :id")
    suspend fun getDownloadById(id: Long): DownloadItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDownload(item: DownloadItem): Long

    @Update
    suspend fun updateDownload(item: DownloadItem)

    @Delete
    suspend fun deleteDownload(item: DownloadItem)

    @Query("DELETE FROM download_items WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("UPDATE download_items SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Long, status: String)

    @Query("UPDATE download_items SET status = 'PAUSED' WHERE status = 'DOWNLOADING'")
    suspend fun pauseAllActive()

    @Query("UPDATE download_items SET status = 'DOWNLOADING' WHERE status = 'PAUSED'")
    suspend fun resumeAllPaused()

    @Query("DELETE FROM download_items WHERE status = 'COMPLETED'")
    suspend fun clearCompleted()

    @Query("DELETE FROM download_items WHERE status IN ('QUEUED', 'DOWNLOADING', 'PAUSED')")
    suspend fun cancelAllActive()
}
