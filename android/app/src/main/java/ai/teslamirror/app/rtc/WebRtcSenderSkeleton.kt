package ai.teslamirror.app.rtc

import kotlinx.serialization.json.JsonElement

class WebRtcSenderSkeleton {
    private var activeSessionId: String? = null
    private var localOfferSdp: String? = null
    private var remoteAnswerSdp: String? = null
    private val remoteCandidates = mutableListOf<JsonElement>()

    fun startSession(sessionId: String) {
        activeSessionId = sessionId
        localOfferSdp = null
        remoteAnswerSdp = null
        remoteCandidates.clear()
        // TODO: create PeerConnectionFactory
        // TODO: wire MediaProjection pipeline into video source
        // TODO: create data channel for control and stats
    }

    fun ensureLocalOffer(sessionId: String): String {
        if (activeSessionId != sessionId) {
            startSession(sessionId)
        }
        if (localOfferSdp == null) {
            localOfferSdp = buildFakeOffer(sessionId)
        }
        return localOfferSdp!!
    }

    fun acceptRemoteAnswer(sessionId: String?, sdp: String?) {
        if (sessionId == null || sessionId != activeSessionId) return
        remoteAnswerSdp = sdp
    }

    fun acceptRemoteIceCandidate(sessionId: String?, candidate: JsonElement?) {
        if (sessionId == null || sessionId != activeSessionId || candidate == null) return
        remoteCandidates += candidate
    }

    fun stopSession() {
        activeSessionId = null
        localOfferSdp = null
        remoteAnswerSdp = null
        remoteCandidates.clear()
        // TODO: dispose peer connection and tracks
    }

    fun getState(): String {
        return when {
            activeSessionId == null -> "idle"
            localOfferSdp == null -> "session_preparing"
            remoteAnswerSdp == null -> "offer_sent"
            else -> "answer_received"
        }
    }

    private fun buildFakeOffer(sessionId: String): String {
        return "v=0\r\no=- 0 0 IN IP4 127.0.0.1\r\ns=TeslaMirror-$sessionId\r\nt=0 0\r\na=group:BUNDLE 0\r\na=msid-semantic: WMS\r\n"
    }
}
