package ai.teslamirror.app.local

import ai.teslamirror.app.rtc.BridgeMessage
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class SignalMessage(
    val type: String,
    val sessionId: String? = null,
    val token: String? = null,
    val sdp: String? = null,
    val candidate: JsonElement? = null,
    val value: String? = null,
    val code: String? = null,
    val message: String? = null,
)

fun BridgeMessage.toSignalMessage(): SignalMessage {
    return SignalMessage(
        type = type,
        sessionId = sessionId,
        sdp = sdp,
        candidate = candidate,
        value = value,
        code = code,
        message = message,
    )
}
