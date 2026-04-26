package ai.teslamirror.app.rtc

class WebRtcSenderSkeleton {
    private var activeSessionId: String? = null

    fun startSession(sessionId: String) {
        activeSessionId = sessionId
        // TODO: create PeerConnectionFactory
        // TODO: wire MediaProjection pipeline into video source
        // TODO: create data channel for control and stats
        // TODO: create SDP offer and pass through local signaling server
    }

    fun stopSession() {
        activeSessionId = null
        // TODO: dispose peer connection and tracks
    }

    fun getState(): String {
        return if (activeSessionId == null) "idle" else "session_preparing"
    }
}
