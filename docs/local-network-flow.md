# Local Network Flow

## 사용자 흐름
1. 사용자가 Android 앱에서 미러링 시작
2. 앱이 핫스팟 상태를 확인
3. 핫스팟이 켜져 있으면 로컬 서버 시작
4. 앱이 접속 URL과 pairing token 표시
5. Tesla가 폰 핫스팟에 연결
6. Tesla 브라우저에서 URL 접속
7. Web client가 signaling 연결
8. WebRTC 협상 완료
9. 화면 표시 시작

## 앱 UI에 필요한 요소
- Hotspot ON/OFF 상태
- Phone local IP
- 접속 포트
- 접속 URL
- Pairing token
- QR code
- Tesla connected 상태
- Current FPS / bitrate / latency

## 실패 케이스
- 핫스팟이 꺼져 있음
- Tesla가 다른 네트워크에 붙어 있음
- 로컬 HTTP 접속 차단
- WebRTC 협상 실패
- H.264 재생 실패

## 대응
- 핫스팟 상태 안내
- 네트워크 재확인 UI
- 세션 재시작 버튼
- 디버그 정보 표시
