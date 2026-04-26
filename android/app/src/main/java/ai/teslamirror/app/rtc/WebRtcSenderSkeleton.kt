package ai.teslamirror.app.rtc

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

class WebRtcSenderSkeleton(
    private val webRtcStack: WebRtcStack = PlaceholderWebRtcStack(),
) {
    private var activeSessionId: String? = null
    private var localOfferSdp: String? = null
    private var remoteAnswerSdp: String? = null
    private val remoteCandidates = mutableListOf<JsonElement>()
    private val json = Json

    fun startSession(sessionId: String) {
        activeSessionId = sessionId
        localOfferSdp = null
        remoteAnswerSdp = null
        remoteCandidates.clear()
        webRtcStack.initialize()
    }

    fun ensureLocalOffer(sessionId: String): String {
        if (activeSessionId != sessionId) {
            startSession(sessionId)
        }
        if (localOfferSdp == null) {
            localOfferSdp = webRtcStack.createOffer(sessionId)
        }
        return localOfferSdp!!
    }

    fun acceptRemoteAnswer(sessionId: String?, sdp: String?) {
        if (sessionId == null || sessionId != activeSessionId) return
        remoteAnswerSdp = sdp
        webRtcStack.applyAnswer(sessionId, sdp)
    }

    fun acceptRemoteIceCandidate(sessionId: String?, candidate: JsonElement?) {
        if (sessionId == null || sessionId != activeSessionId || candidate == null) return
        remoteCandidates += candidate
        webRtcStack.addIceCandidate(sessionId, json.encodeToString(candidate))
    }

    fun stopSession() {
        activeSessionId = null
        localOfferSdp = null
        remoteAnswerSdp = null
        remoteCandidates.clear()
        webRtcStack.dispose()
    }

    fun getState(): String {
        return when {
            activeSessionId == null -> "idle"
            localOfferSdp == null -> "session_preparing"
            remoteAnswerSdp == null -> "offer_sent"
            else -> "answer_received"
        }
    }
}
