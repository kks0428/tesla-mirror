package ai.teslamirror.app.rtc

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class SignalingBridge(
    private val rtc: WebRtcSenderSkeleton,
) {
    fun createOfferMessage(sessionId: String): BridgeMessage {
        val sdp = rtc.ensureLocalOffer(sessionId)
        return BridgeMessage(
            type = "session.offer",
            sessionId = sessionId,
            sdp = sdp,
        )
    }

    fun onAnswer(sessionId: String?, sdp: String?): BridgeMessage {
        rtc.acceptRemoteAnswer(sessionId, sdp)
        return BridgeMessage(
            type = "session.state",
            sessionId = sessionId,
            value = "answer_applied_placeholder",
        )
    }

    fun onIceCandidate(sessionId: String?, candidate: JsonElement?): BridgeMessage {
        rtc.acceptRemoteIceCandidate(sessionId, candidate)
        return BridgeMessage(
            type = "session.state",
            sessionId = sessionId,
            value = "ice_applied_placeholder",
        )
    }

    fun statsSnapshot(sessionId: String?): List<BridgeMessage> {
        val state = rtc.getState()
        return listOf(
            BridgeMessage(type = "session.state", sessionId = sessionId, value = state),
            BridgeMessage(type = "stats.fps", sessionId = sessionId, value = "0"),
            BridgeMessage(type = "stats.bitrate", sessionId = sessionId, value = "0"),
        )
    }
}

data class BridgeMessage(
    val type: String,
    val sessionId: String? = null,
    val sdp: String? = null,
    val candidate: JsonElement? = null,
    val value: String? = null,
    val code: String? = null,
    val message: String? = null,
)
