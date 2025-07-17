---
name: Bug report
about: Create a report to help us improve
title: ''
labels: ''
assignees: ''

---

name: "🐞 버그 제보"
title: "[BUG]: "
labels: ["bug"]
assignees: []

body:
  - type: markdown
    attributes:
      value: |
        🐞 버그 제보 양식입니다. 구체적으로 작성해주세요

  - type: textarea
    id: what-happened
    attributes:
      label: 어떤 문제가 발생했나요?
      description: 기대한 동작과 실제 발생한 문제를 함께 적어주세요.
      placeholder: 예) 로그인 시도 시 무한 로딩이 발생합니다.
    validations:
      required: true

## 🎯 목표 및 해결 방향

- 이 이슈를 통해 달성하고자 하는 목표나 기대 효과를 작성해주세요.
> 예: 사용자에게 부족한 금액과 함께 에러 메시지를 반환
