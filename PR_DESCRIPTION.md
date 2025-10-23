## 🔖 PR 유형
- [x] ✨ 기능 추가
- [ ] 🐛 버그 수정
- [ ] ♻️ 리팩토링
- [ ] 🧪 테스트 코드 추가
- [ ] 📄 문서 수정
- [ ] 기타

## 📌 개요
포트폴리오 비율 리밸런싱 기능을 추가하고 관련 API를 제공합니다.

## 🔧 작업 내용
- 리밸런싱 도메인 추가: `rebalance` 패키지 구조 구성
  - 컨트롤러: `rebalance.controller.RebalanceController`
  - 서비스: `rebalance.service.RebalanceService`
  - 매퍼: `rebalance.mapper.RebalanceMapper`
  - VO/엔티티: `rebalance.vo.Rebalance`
  - 응답 DTO 추가: `rebalance.dto.res.*`
- 투자 성향 관련 enum 추가: `rebalance.enums.InvestType`
- 포트폴리오 리밸런싱 비중 계산 및 추천 로직 구현
- API 엔드포인트 제공 및 응답 스펙 정의

## ✅ 체크리스트
- [ ] 테스트 완료(Postman, Swagger)

## 📝 기타 참고 사항
- 입력 파라미터 검증 및 엣지 케이스(합계 100% 보정, 반올림 정책) 재검토 필요
- 통합 테스트 및 부하 테스트는 후속 PR에서 보강 예정

## 📎 관련 이슈
Close #이슈번호
