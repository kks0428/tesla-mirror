# Next Steps

## 1차 구현 순서
1. Android 내장 HTTP/WS 서버 최소 구현
2. Tesla web receiver 정적 페이지 구성
3. Android WebRTC sender skeleton
4. H.264 video track 연결
5. 로컬 핫스팟 환경 end-to-end 연결 테스트
6. latency/stats 표시

## 즉시 검증할 가설
1. Tesla 브라우저가 로컬 IP `http://phone-ip:port` 접속을 허용하는가
2. Tesla 브라우저가 WebRTC + H.264를 안정적으로 재생하는가
3. autoplay 제약이 어느 정도인가
4. DataChannel 입력 지연이 충분히 낮은가

## 구현 원칙
- 일단 영상 먼저
- 오디오는 나중
- 입력은 최소 기능부터
- 핫스팟 자동 제어는 나중
- Shizuku는 아직 넣지 않음
