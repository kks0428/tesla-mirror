# Implementation Roadmap

## Current code status
- Android local HTTP/WebSocket server skeleton exists
- Tesla browser web client skeleton exists
- Session coordinator exists
- MediaProjection request controller placeholder exists
- WebRTC sender placeholder exists

## Next engineering steps
1. Replace `startActivity(intent)` with Activity Result API for MediaProjection permission flow
2. Introduce a real embedded HTTP/WebSocket server lifecycle tied to foreground service
3. Add real WebRTC library integration on Android
4. Connect signaling messages to the WebRTC sender
5. Generate and forward SDP offer
6. Accept browser answer and ICE candidates
7. Stream a real video track
8. Add diagnostics and session recovery

## Build caveats
- Android app should be built on x86_64 machine due to AAPT2 host binary issues on Raspberry Pi ARM64
