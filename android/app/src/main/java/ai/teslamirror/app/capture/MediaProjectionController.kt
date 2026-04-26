package ai.teslamirror.app.capture

import android.app.Activity
import android.content.Intent
import android.media.projection.MediaProjectionManager

class MediaProjectionController(
    private val mediaProjectionManager: MediaProjectionManager,
) {
    fun createCaptureIntent(): Intent = mediaProjectionManager.createScreenCaptureIntent()

    fun describeResult(resultCode: Int): String {
        return if (resultCode == Activity.RESULT_OK) "granted" else "denied"
    }
}
