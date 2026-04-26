package ai.teslamirror.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ai.teslamirror.app.local.LocalServerSkeleton
import ai.teslamirror.app.net.LocalIpResolver

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val server = LocalServerSkeleton(this)

        setContent {
            val status = remember { mutableStateOf("idle") }
            val url = remember { mutableStateOf("http://${LocalIpResolver.guessHotspotIp()}:8080") }
            val token = remember { mutableStateOf("-") }
            val sessionId = remember { mutableStateOf("-") }
            MaterialTheme {
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Tesla Mirror")
                    Text("Status: ${status.value}")
                    Text("Mode: hotspot local WebRTC")
                    Text("URL: ${url.value}")
                    Text("Session: ${sessionId.value}")
                    Text("Token: ${token.value}")
                    Button(onClick = {
                        server.start()
                        val session = server.createSession()
                        sessionId.value = session.sessionId
                        token.value = session.token
                        val ip = LocalIpResolver.guessHotspotIp()
                        url.value = "http://${ip}:8080/?sessionId=${session.sessionId}&token=${session.token}"
                        status.value = "local server started"
                    }) {
                        Text("Start Mirror")
                    }
                    Button(onClick = {
                        server.stop()
                        status.value = "local server stopped"
                    }) {
                        Text("Stop Mirror")
                    }
                }
            }
        }
    }
}
