# Core vs Shizuku Boundaries

## Core에 넣는 것
- MediaProjection 화면 캡처
- MediaCodec 인코딩
- WebRTC 송출
- 세션 관리
- Tesla 브라우저 클라이언트
- 기본 진단 정보

## 나중에 Shizuku로 확장할 것
- 핫스팟 시작/정지 보조
- 설정 토글 보조
- 앱 실행/강제 종료 보조
- 일부 시스템 API 접근
- 기기 자동화

## Core에서 절대 바로 의존하지 않을 것
- WRITE_SECURE_SETTINGS
- root
- hidden API 직접 난사
- 제조사 전용 비공개 API 강결합

## 원칙
- 미러링이 제품의 본체
- Shizuku는 선택 확장
- 코어 기능은 일반 사용자도 실행 가능해야 함
