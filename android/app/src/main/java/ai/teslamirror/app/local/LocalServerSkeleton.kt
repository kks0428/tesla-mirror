package ai.teslamirror.app.local

import android.content.Context
import ai.teslamirror.app.rtc.SignalingBridge
import io.ktor.http.ContentType
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.websocket.Frame
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.webSocket
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class LocalServerSkeleton(
    private val context: Context,
    private val signalingBridge: SignalingBridge,
    val port: Int = 8080,
) {
    private val json = Json { ignoreUnknownKeys = true }
    private var engine: ApplicationEngine? = null
    private val sessions = LocalSessionRegistry()

    fun createSession(): LocalSession = sessions.createSession()

    fun start() {
        if (engine != null) return
        engine = embeddedServer(CIO, port = port) {
            install(WebSockets)
            routing {
                get("/") {
                    call.respondAsset("web/index.html", ContentType.Text.Html)
                }
                get("/app.js") {
                    call.respondAsset("web/app.js", ContentType.Application.JavaScript)
                }
                get("/app.css") {
                    call.respondAsset("web/app.css", ContentType.Text.CSS)
                }
                get("/health") {
                    call.respondText("ok")
                }
                webSocket("/signal") {
                    sendSerialized(SignalMessage(type = "session.state", value = "socket_connected"))
                    incoming.consumeEach { frame ->
                        if (frame is Frame.Text) {
                            val text = frame.readText()
                            val msg = runCatching { json.decodeFromString<SignalMessage>(text) }.getOrNull()
                            if (msg == null) {
                                sendSerialized(SignalMessage(type = "error", code = "invalid_json", message = "Could not parse message"))
                                return@consumeEach
                            }
                            when (msg.type) {
                                "session.join" -> {
                                    if (!sessions.validate(msg.sessionId, msg.token)) {
                                        sendSerialized(SignalMessage(type = "error", sessionId = msg.sessionId, code = "invalid_token", message = "Session token mismatch"))
                                    } else {
                                        sendSerialized(SignalMessage(type = "session.state", sessionId = msg.sessionId, value = "joined"))
                                        if (msg.sessionId != null) {
                                            sendSerialized(signalingBridge.createOfferMessage(msg.sessionId).toSignalMessage())
                                            signalingBridge.statsSnapshot(msg.sessionId).forEach { snapshot ->
                                                sendSerialized(snapshot.toSignalMessage())
                                            }
                                        }
                                    }
                                }
                                "session.answer" -> {
                                    sendSerialized(signalingBridge.onAnswer(msg.sessionId, msg.sdp).toSignalMessage())
                                }
                                "session.ice-candidate" -> {
                                    sendSerialized(signalingBridge.onIceCandidate(msg.sessionId, msg.candidate).toSignalMessage())
                                }
                                else -> {
                                    sendSerialized(SignalMessage(type = "session.state", sessionId = msg.sessionId, value = "received:${msg.type}"))
                                }
                            }
                        }
                    }
                }
            }
        }.start(wait = false)
    }

    fun stop() {
        engine?.stop()
        engine = null
    }

    private suspend fun io.ktor.server.websocket.DefaultWebSocketServerSession.sendSerialized(msg: SignalMessage) {
        send(Frame.Text(json.encodeToString(msg)))
    }

    private suspend fun io.ktor.server.application.ApplicationCall.respondAsset(path: String, contentType: ContentType) {
        val text = context.assets.open(path).bufferedReader().use { it.readText() }
        respondText(text, contentType)
    }
}
