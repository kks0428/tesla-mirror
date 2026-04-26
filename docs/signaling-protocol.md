# Signaling Protocol

## 전송
- Android app 내장 WebSocket 서버 사용
- Web client는 같은 로컬 네트워크에서 접속
- JSON 메시지 사용

## 공통 필드
- type
- sessionId
- timestamp

## 메시지

### session.create
Android 내부에서 로컬 세션 생성 시 사용.

```json
{ "type": "session.create", "deviceName": "Pixel 8" }
```

### session.created
```json
{ "type": "session.created", "sessionId": "abc123", "token": "secret", "expiresIn": 300, "url": "http://192.168.43.1:8080" }
```

### session.join
```json
{ "type": "session.join", "sessionId": "abc123", "token": "secret" }
```

### session.offer
```json
{ "type": "session.offer", "sessionId": "abc123", "sdp": "..." }
```

### session.answer
```json
{ "type": "session.answer", "sessionId": "abc123", "sdp": "..." }
```

### session.ice-candidate
```json
{ "type": "session.ice-candidate", "sessionId": "abc123", "candidate": { "candidate": "...", "sdpMid": "0", "sdpMLineIndex": 0 } }
```

### session.close
```json
{ "type": "session.close", "sessionId": "abc123", "reason": "browser_disconnected" }
```

### error
```json
{ "type": "error", "sessionId": "abc123", "code": "invalid_token", "message": "..." }
```
