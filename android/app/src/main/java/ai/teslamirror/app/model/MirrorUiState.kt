package ai.teslamirror.app.model

data class MirrorUiState(
    val status: String = "idle",
    val localIp: String = "phone-ip",
    val url: String = "",
    val sessionId: String = "-",
    val token: String = "-",
    val serverRunning: Boolean = false,
    val rtcState: String = "not_started",
    val projectionState: String = "not_granted",
)
