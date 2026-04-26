package ai.teslamirror.app

import android.content.Context
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ai.teslamirror.app.capture.MediaProjectionController
import ai.teslamirror.app.local.LocalServerSkeleton
import ai.teslamirror.app.rtc.WebRtcSenderSkeleton
import ai.teslamirror.app.session.MirrorSessionCoordinator

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val server = LocalServerSkeleton(this)
        val rtc = WebRtcSenderSkeleton()
        val mediaProjectionController = MediaProjectionController(
            getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        )
        val coordinator = MirrorSessionCoordinator(server, rtc, mediaProjectionController)

        val projectionLauncher = registerForActivityResult(StartActivityForResult()) { result ->
            coordinator.onProjectionPermissionResult(result.resultCode, result.data)
        }

        setContent {
            val uiState by coordinator.uiState.collectAsState()
            MaterialTheme {
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Tesla Mirror")
                    Text("Status: ${uiState.status}")
                    Text("Mode: hotspot local WebRTC")
                    Text("Projection: ${uiState.projectionState}")
                    Text("RTC: ${uiState.rtcState}")
                    Text("IP: ${uiState.localIp}")
                    Text("URL: ${uiState.url.ifBlank { "http://${uiState.localIp}:8080" }}")
                    Text("Session: ${uiState.sessionId}")
                    Text("Token: ${uiState.token}")
                    Button(onClick = {
                        coordinator.startLocalSession()
                    }) {
                        Text("Start Mirror")
                    }
                    Button(onClick = {
                        coordinator.stopLocalSession()
                    }) {
                        Text("Stop Mirror")
                    }
                    Button(onClick = {
                        val intent = mediaProjectionController.createCaptureIntent()
                        projectionLauncher.launch(intent)
                        coordinator.updateProjectionState("requesting")
                    }) {
                        Text("Request Screen Capture")
                    }
                }
            }
        }
    }
}
