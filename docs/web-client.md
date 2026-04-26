# Tesla Web Client

## 역할
- Android 폰의 로컬 주소로 접속
- WebRTC receiver 역할 수행
- 영상 표시
- DataChannel로 입력 전송
- 상태 HUD 표시

## 주요 컴포넌트
- app shell
- signaling client
- rtc receiver
- video view
- input mapper
- diagnostics overlay

## 입력 처리
- 터치 좌표를 video viewport 기준으로 정규화
- Android 원본 해상도와 매핑
- 초기에는 click/tap 위주
- drag/gesture는 후순위

## 표시 정보
- connected/disconnected
- RTT
- FPS
- bitrate
- resolution
- local session id

## 브라우저 호환성 체크
- 로컬 IP HTTP 접속 허용 여부
- autoplay 정책
- inline video 가능 여부
- H.264 decode 지원
- DataChannel 안정성
