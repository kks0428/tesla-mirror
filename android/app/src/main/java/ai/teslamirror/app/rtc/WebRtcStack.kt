package ai.teslamirror.app.rtc

interface WebRtcStack {
    fun initialize()
    fun createOffer(sessionId: String): String
    fun applyAnswer(sessionId: String, sdp: String?)
    fun addIceCandidate(sessionId: String, candidateJson: String)
    fun dispose()
}

class PlaceholderWebRtcStack : WebRtcStack {
    override fun initialize() {
    }

    override fun createOffer(sessionId: String): String {
        return "v=0\r\no=- 0 0 IN IP4 127.0.0.1\r\ns=TeslaMirror-$sessionId\r\nt=0 0\r\na=group:BUNDLE 0\r\na=msid-semantic: WMS\r\n"
    }

    override fun applyAnswer(sessionId: String, sdp: String?) {
    }

    override fun addIceCandidate(sessionId: String, candidateJson: String) {
    }

    override fun dispose() {
    }
}
