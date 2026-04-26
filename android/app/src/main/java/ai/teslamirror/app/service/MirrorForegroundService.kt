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
        MirrorServiceState.update("service_created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            MirrorServiceController.ACTION_START -> {
                MirrorServiceState.update("service_started")
                refreshNotification("Local mirror session starting")
            }
            MirrorServiceController.ACTION_STOP -> {
                MirrorServiceState.update("service_stopping")
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
            else -> {
                MirrorServiceState.update("service_running")
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        MirrorServiceState.update("service_destroyed")
        super.onDestroy()
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

    private fun refreshNotification(text: String) {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(1, buildNotification(text))
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
