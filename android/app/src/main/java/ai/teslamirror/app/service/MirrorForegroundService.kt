package ai.teslamirror.app.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder

class MirrorForegroundService : Service() {

    override fun onCreate() {
        super.onCreate()
        createChannel()
        startForeground(1, buildNotification("Tesla Mirror is idle"))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(
                NotificationChannel("mirror", "Tesla Mirror", NotificationManager.IMPORTANCE_LOW)
            )
        }
    }

    private fun buildNotification(text: String): Notification {
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, "mirror")
        } else {
            Notification.Builder(this)
        }
        return builder
            .setContentTitle("Tesla Mirror")
            .setContentText(text)
            .setSmallIcon(android.R.drawable.presence_video_online)
            .build()
    }
}
