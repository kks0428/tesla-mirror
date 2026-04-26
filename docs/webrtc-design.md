# WebRTC Design

## 목표
Tesla 브라우저가 Android Sender App의 화면 스트림을 폰 핫스팟 내부 네트워크에서 직접 수신한다.

## 연결 모델
- Sender: Android app
- Receiver: Tesla browser client
- Signaling: Android app 내부 WebSocket 서버
- Media path: WebRTC PeerConnection
- Control path: WebRTC DataChannel

## 미디어 전략
### Video
- Codec: H.264 우선
- Source: MediaProjection -> Surface -> MediaCodec
- Target resolution: 1280x720 기본
- Adaptive bitrate: 로컬 네트워크 상태에 따라 조정
- Initial FPS target: 30fps

### Audio
- 초기 단계에서는 제외 또는 실험 기능

## Peer roles
- Android app가 offer 생성
- Tesla browser가 answer 생성
- 재연결은 브라우저 새로고침과 앱 reconnect로 단순화

## ICE 전략
- host candidate 우선
- 동일 핫스팟 LAN 기준 최적화
- 초기에는 STUN/TURN 미사용
- 필요 시 fallback 확장

## Signaling 메시지
- session.create
- session.offer
- session.answer
- session.ice-candidate
- session.close
- telemetry.ping
- telemetry.pong

## DataChannel 메시지
### Browser -> Android
- input.tap
- input.down
- input.move
- input.up
- input.key
- client.viewport
- client.ping

### Android -> Browser
- stats.latency
- stats.bitrate
- stats.fps
- session.state
- error

## 연결 순서
1. Android app가 로컬 세션 시작
2. 로컬 웹서버와 signaling 서버 기동
3. 앱이 접속 주소와 token 표시
4. Tesla browser가 `http://phone-ip:port` 접속
5. Browser가 session.join 수행
6. Android app가 SDP offer 생성
7. Browser가 SDP answer 반환
8. ICE candidate 교환
9. 영상 스트림 수립
10. DataChannel open

## 진단 정보
- RTT
- frame encode time
- delivered FPS
- current bitrate
- packet loss
- candidate pair

## 보안
- session token 필수
- 세션 TTL 짧게 유지
- 동일 핫스팟 내 임의 접속 방지
- 가능하면 접속 후 1회 pairing 사용
