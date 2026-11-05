package com.example.engine

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.MainActivity
import com.example.R

class DownloadNotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "searx_downloads_channel"
        const val NOTIFICATION_ID_BASE = 1000
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.channel_name_downloads)
            val desc = context.getString(R.string.channel_desc_downloads)
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = desc
                setShowBadge(false)
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showProgressNotification(
        downloadId: Long,
        title: String,
        progress: Int,
        speed: String
    ) {
        try {
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setContentTitle(title)
                .setContentText("$progress% • $speed")
                .setProgress(100, progress, false)
                .setOngoing(true)
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .setPriority(NotificationCompat.PRIORITY_LOW)

            val manager = NotificationManagerCompat.from(context)
            manager.notify((NOTIFICATION_ID_BASE + downloadId).toInt(), builder.build())
        } catch (_: SecurityException) {
            // Notification permission might not be granted yet on Android 13+
        }
    }

    fun showCompletedNotification(
        downloadId: Long,
        title: String,
        fileSizeFormatted: String
    ) {
        try {
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                .setContentTitle("Download Complete: $title")
                .setContentText("Saved to Downloads • $fileSizeFormatted")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            val manager = NotificationManagerCompat.from(context)
            manager.notify((NOTIFICATION_ID_BASE + downloadId).toInt(), builder.build())
        } catch (_: SecurityException) {
            // Notification permission not granted
        }
    }

    fun cancelNotification(downloadId: Long) {
        try {
            val manager = NotificationManagerCompat.from(context)
            manager.cancel((NOTIFICATION_ID_BASE + downloadId).toInt())
        } catch (_: Exception) {
        }
    }
}
