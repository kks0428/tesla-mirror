# Implementation Roadmap

## Current code status
- Android local HTTP/WebSocket server skeleton exists
- Tesla browser web client skeleton exists
- Session coordinator exists
- MediaProjection request controller placeholder exists
- WebRTC sender placeholder exists

## Next engineering steps
1. MediaProjection permission flow moved to Activity Result API
2. Basic foreground service lifecycle wiring added
3. Basic signaling bridge wiring added between local server and sender skeleton
4. Introduce a real embedded HTTP/WebSocket server lifecycle tied fully to foreground service
5. Add real WebRTC library integration on Android
6. Replace fake SDP offer generation with real PeerConnection offer
7. Accept browser answer and ICE candidates in real peer connection
8. Stream a real video track
9. Add diagnostics and session recovery

## Build caveats
- Android app should be built on x86_64 machine due to AAPT2 host binary issues on Raspberry Pi ARM64
