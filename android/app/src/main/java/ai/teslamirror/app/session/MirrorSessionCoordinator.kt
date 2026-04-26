package ai.teslamirror.app.session

import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjection
import ai.teslamirror.app.capture.MediaProjectionController
import ai.teslamirror.app.local.LocalServerSkeleton
import ai.teslamirror.app.model.MirrorUiState
import ai.teslamirror.app.net.LocalIpResolver
import ai.teslamirror.app.rtc.WebRtcSenderSkeleton
import ai.teslamirror.app.service.MirrorServiceController
import ai.teslamirror.app.service.MirrorServiceState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MirrorSessionCoordinator(
    private val context: Context,
    private val server: LocalServerSkeleton,
    private val rtc: WebRtcSenderSkeleton,
    private val projectionController: MediaProjectionController,
) {
    private val _uiState = MutableStateFlow(MirrorUiState(localIp = LocalIpResolver.guessHotspotIp()))
    val uiState: StateFlow<MirrorUiState> = _uiState.asStateFlow()
    private var mediaProjection: MediaProjection? = null

    fun startLocalSession() {
        MirrorServiceController.start(context)
        server.start()
        val session = server.createSession()
        val ip = LocalIpResolver.guessHotspotIp()
        val url = "http://$ip:${server.port}/?sessionId=${session.sessionId}&token=${session.token}"
        rtc.startSession(session.sessionId)
        _uiState.value = _uiState.value.copy(
            status = MirrorServiceState.status.value,
            localIp = ip,
            url = url,
            sessionId = session.sessionId,
            token = session.token,
            serverRunning = true,
            rtcState = "starting",
        )
    }

    fun stopLocalSession() {
        rtc.stopSession()
        server.stop()
        MirrorServiceController.stop(context)
        _uiState.value = _uiState.value.copy(
            status = "local session stopped",
            serverRunning = false,
            rtcState = "stopped",
        )
    }

    fun updateProjectionState(value: String) {
        _uiState.value = _uiState.value.copy(projectionState = value)
    }

    fun onProjectionPermissionResult(resultCode: Int, data: Intent?) {
        val projection = projectionController.acquireProjection(resultCode, data)
        mediaProjection = projection
        _uiState.value = _uiState.value.copy(
            projectionState = if (projection != null) "granted" else "denied"
        )
    }

    fun hasProjection(): Boolean = mediaProjection != null
}
