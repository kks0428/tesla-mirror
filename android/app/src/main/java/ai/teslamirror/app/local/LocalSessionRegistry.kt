package ai.teslamirror.app.local

import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

data class LocalSession(
    val sessionId: String,
    val token: String,
)

class LocalSessionRegistry {
    private val sessions = ConcurrentHashMap<String, LocalSession>()

    fun createSession(): LocalSession {
        val session = LocalSession(
            sessionId = UUID.randomUUID().toString(),
            token = UUID.randomUUID().toString().replace("-", "").take(12),
        )
        sessions[session.sessionId] = session
        return session
    }

    fun get(sessionId: String?): LocalSession? = sessionId?.let { sessions[it] }

    fun validate(sessionId: String?, token: String?): Boolean {
        val session = get(sessionId) ?: return false
        return session.token == token
    }
}
