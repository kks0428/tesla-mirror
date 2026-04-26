package ai.teslamirror.app.service

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

object MirrorServiceController {
    const val ACTION_START = "ai.teslamirror.app.action.START"
    const val ACTION_STOP = "ai.teslamirror.app.action.STOP"

    fun start(context: Context) {
        val intent = Intent(context, MirrorForegroundService::class.java).apply {
            action = ACTION_START
        }
        ContextCompat.startForegroundService(context, intent)
    }

    fun stop(context: Context) {
        val intent = Intent(context, MirrorForegroundService::class.java).apply {
            action = ACTION_STOP
        }
        context.startService(intent)
    }
}
