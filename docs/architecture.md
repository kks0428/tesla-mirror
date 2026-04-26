# Architecture

## 제품 목표
Tesla 브라우저가 Android 폰의 핫스팟에 접속한 뒤, 폰 내부 네트워크에서 안드로이드 화면을 저지연으로 수신한다.

## 상위 구성
1. Android Sender App
2. Embedded Local Server inside Android app
3. Tesla Browser Client
4. Optional Shizuku Extension Layer

## 연결 모델
- Android 폰이 핫스팟 호스트 역할 수행
- Tesla 차량이 핫스팟 클라이언트로 접속
- Android 앱이 로컬 HTTP + WebSocket signaling 서버를 직접 제공
- Media path는 WebRTC peer connection 사용
- Control path는 WebRTC DataChannel 사용

## Android Sender App
### 책임
- MediaProjection으로 화면 캡처
- MediaCodec으로 비디오 인코딩
- Foreground Service 유지
- 로컬 IP 및 핫스팟 세션 상태 관리
- 내장 HTTP/WS 서버 실행
- WebRTC sender 역할 수행

### 내부 모듈
- capture
- encoder-video
- encoder-audio
- local-server
- transport-rtc
- signaling-local
- hotspot-session
- telemetry
- ui
- privileged-extension-interface

## Embedded Local Server
### 책임
- Tesla 브라우저용 web client 정적 파일 제공
- signaling WebSocket 처리
- pairing token 검증
- 세션 수명 관리

### 제공 엔드포인트 예시
- `GET /`
- `GET /app.js`
- `GET /app.css`
- `WS /signal`
- `GET /health`

## Tesla Browser Client
### 책임
- 폰 로컬 주소로 접속
- WebRTC receiver 역할 수행
- 영상 표시
- 입력 이벤트를 DataChannel로 전송
- 상태 HUD 표시

## 전송 전략
### 최종 목표
- WebRTC 사용
- Video codec: H.264 우선
- Audio: 후순위
- ICE는 로컬 네트워크 host candidate 우선

### 초기 전략
- 같은 핫스팟 내부 네트워크만 우선 지원
- STUN/TURN 없이 시작 가능성 검증
- 필요 시 나중에 외부 네트워크 모드 추가

## 권한 계층
### Core mode
- 핫스팟이 이미 켜져 있을 때 미러링 동작
- Shizuku 없이 실행 가능

### Extended mode
- Shizuku 사용 가능 시 핫스팟 시작/정지 보조
- 설정 토글 보조
- 기기 자동화 보조

## 주요 리스크
- Tesla 브라우저의 로컬 IP 접속 허용 여부
- Tesla 브라우저의 WebRTC/H.264 호환성
- secure context 요구 여부
- 발열, 배터리, 장시간 인코딩 안정성
