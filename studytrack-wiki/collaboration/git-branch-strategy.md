# 🌿 Git 브랜치 및 커밋 전략

## 📖 개요
StudyTrack 프로젝트는 **Git Flow**를 기반으로, 명확한 **브랜치 명명 규칙**과 **커밋 메시지 컨벤션**을 사용합니다.  
혼자 개발하더라도 체계적인 버전 관리를 통해 안정적인 개발과 배포를 보장하는 것을 목표로 합니다.

---

## 🌳 브랜치 구조 (Git Flow)

```
main (운영)
├── hotfix/emergency-fix
└── develop (개발 통합)
    ├── feature/user-auth
    ├── feature/study-management
    └── release/v1.0.0
```

### **브랜치 역할**

| 브랜치 | 역할 | 상태 | 소스 | 대상 |
|---|---|---|---|---|
| **main** | 운영 배포 | 항상 안정 | release, hotfix | - |
| **develop** | 개발 통합 | 개발 완료 기능들 | feature | main |
| **feature/** | 기능 개발 | 개발 중 | develop | develop |
| **release/** | 릴리스 준비 | 배포 준비 | develop | main, develop |
| **hotfix/** | 긴급 수정 | 버그 수정 | main | main, develop |

---

## 📋 브랜치 명명 규칙

### **기본 형식**
```
[type]/#[이슈번호]-[간단한-설명]
```

### **Type 종류**

브랜치를 생성할 때, 작업의 성격에 맞는 `type`을 접두사로 사용합니다. 이 `type`은 커밋 메시지의 `type`과 일치해야 합니다.

| Type | 설명 |
|---|---|
| **feature** | 새로운 기능 개발 |
| **fix** | 버그 수정 |
| **docs** | 문서 수정 (README, wiki 등) |
| **style** | 코드 포맷팅, 세미콜론 누락 등 (기능 변경 없음) |
| **refactor** | 코드 리팩토링 (성능 개선, 구조 변경 등) |
| **test** | 테스트 코드 추가 또는 수정 |
| **chore** | 빌드, 패키지 매니저 설정 등 (운영 코드와 무관) |
| **hotfix** | 운영 환경의 긴급 버그 수정 |
| **release** | 릴리스 준비 |

### **예시**
```bash
# 기능 개발
feature/#42-user-authentication

# 문서 수정
docs/#43-update-readme

# 리팩토링
refactor/#44-optimize-query
```

---

## 📝 커밋 메시지 규칙

브랜치의 작업 내용은 작은 단위로 자주 커밋하며, 아래 규칙을 따릅니다.

### **기본 형식**
```
[이모지] [type/#이슈번호] 제목
```
- **이모지**: 커밋 성격을 나타내는 아이콘입니다.
- **type**: 현재 브랜치의 `type`과 일치시킵니다.
- **제목**: 변경사항을 요약하여 작성합니다.

### **주요 이모지**

| 이모지 | 설명 |
|---|---|
| ✨ | 새로운 기능 추가 (feature) |
| 🐛 | 버그 수정 (fix) |
| 📝 | 문서 수정 (docs) |
| ✅ | 테스트 추가/수정 (test) |
| ♻️ | 리팩토링 (refactor) |
| 🎨 | 코드 스타일 개선 (style) |
| 🔥 | 코드/파일 제거 |
| 📦 | 패키지 추가/업데이트 (chore) |
| 🚀 | 배포 관련 (release) |

### **커밋 예시**
```bash
# feature/#42 브랜치에서의 커밋
git commit -m "✨ [feature/#42] 로그인 API 기본 구조 추가"
git commit -m "✅ [test/#42] 로그인 API 테스트 추가"

# fix/#43 브랜치에서의 커밋
git commit -m "🐛 [fix/#43] 로그인 유효성 검사 버그 수정"
```

---

## 🚀 워크플로 및 실전 시나리오

(기존 워크플로, 실전 시나리오, 브랜치 정리, 보호 규칙, 팁, 주의사항 내용은 동일하게 유지됩니다.)

---

> ℹ️ **더 상세한 커밋 메시지 규칙**은 [커밋 메시지 컨벤션](./commitmessage-convention.md) 문서에서 확인할 수 있습니다.