# 🌿 Git 브랜치 전략

## 📖 개요
StudyTrack 프로젝트는 **Git Flow** 기반의 브랜치 전략을 사용합니다.  
혼자 개발하더라도 체계적인 브랜치 관리를 통해 안정적인 개발과 배포를 보장합니다.

---

## 🌳 브랜치 구조

```
main (운영)
├── hotfix/emergency-fix
└── develop (개발 통합)
    ├── feature/user-auth
    ├── feature/study-management
    ├── feature/problem-solving
    └── release/v1.0.0
```

### **브랜치 역할**

| 브랜치 | 역할 | 상태 | 소스 | 대상 |
|--------|------|------|------|------|
| **main** | 운영 배포 | 항상 안정 | release, hotfix | - |
| **develop** | 개발 통합 | 개발 완료 기능들 | feature | main |
| **feature/** | 기능 개발 | 개발 중 | develop | develop |
| **release/** | 릴리스 준비 | 배포 준비 | develop | main, develop |
| **hotfix/** | 긴급 수정 | 버그 수정 | main | main, develop |

---

## 🚀 워크플로

### **1️⃣ 새로운 기능 개발**

```bash
# develop에서 분기
git checkout develop
git pull origin develop

# 새 기능 브랜치 생성
git checkout -b feature/#42-user-login

# 개발 및 커밋
git add .
git commit -m "✨ [feature/#42] 로그인 API 기본 구조 추가"
git commit -m "✨ [feature/#42] JWT 토큰 검증 로직 구현"  
git commit -m "✅ [test/#42] 로그인 API 테스트 추가"

# 기능 완료 후 푸시
git push origin feature/#42-user-login

# GitHub에서 PR 생성: feature/#42-user-login → develop
```

### **2️⃣ 버그 수정**

```bash
# develop에서 분기 (일반적인 버그)
git checkout develop
git checkout -b fix/#43-login-validation

# 또는 main에서 분기 (긴급 버그)
git checkout main  
git checkout -b hotfix/#44-security-patch

# 수정 및 커밋
git commit -m "🐛 [fix/#43] 로그인 유효성 검사 버그 수정"

# PR 생성
git push origin fix/#43-login-validation
```

### **3️⃣ 릴리스 준비**

```bash
# develop에서 릴리스 브랜치 생성
git checkout develop
git checkout -b release/v1.0.0

# 버전 정보 업데이트, 최종 테스트
git commit -m "🚀 [release] v1.0.0 릴리스 준비"

# main으로 머지 후 태그 생성
git checkout main
git merge release/v1.0.0
git tag v1.0.0
git push origin main --tags

# develop에도 백머지
git checkout develop  
git merge release/v1.0.0
```

---

## 📋 브랜치 명명 규칙

### **기본 형식**
```
[type]/#[이슈번호]-[간단한-설명]
```

### **예시**
```bash
# 기능 개발
feature/#42-user-authentication
feature/#43-study-crud-api
feature/#44-problem-solving-ui

# 버그 수정  
fix/#45-login-validation-error
fix/#46-password-encryption-bug

# 긴급 수정
hotfix/#47-security-vulnerability
hotfix/#48-database-connection-issue

# 릴리스
release/v1.0.0
release/v1.1.0-beta
```

---

## ⚡ 브랜치 정리

### **완료된 브랜치 삭제**
```bash
# 로컬 브랜치 삭제
git branch -d feature/#42-user-login

# 원격 브랜치 삭제  
git push origin --delete feature/#42-user-login

# 또는 GitHub에서 PR 머지 시 자동 삭제 설정
```

### **정기적인 정리**
```bash
# 원격에서 삭제된 브랜치 로컬에서 정리
git remote prune origin

# 머지된 브랜치 일괄 삭제
git branch --merged develop | grep -v "develop\|main" | xargs -n 1 git branch -d
```

---

## 🔒 브랜치 보호 규칙

### **GitHub 설정 권장사항**

#### **main 브랜치**
- ✅ Require pull request reviews
- ✅ Require status checks to pass
- ✅ Require linear history
- ✅ Do not allow bypass

#### **develop 브랜치**  
- ✅ Require pull request reviews (선택)
- ✅ Require status checks to pass
- ✅ Allow force pushes (개발 단계)

---

## 🎯 실전 시나리오

### **시나리오 1: 사용자 인증 기능 개발**
```bash
# 1. 이슈 #10 생성: "사용자 로그인/회원가입 API 구현"

# 2. 브랜치 생성
git checkout develop
git checkout -b feature/#10-user-auth

# 3. 단계별 개발
git commit -m "✨ [feature/#10] User 엔티티 생성"
git commit -m "✨ [feature/#10] UserService 로직 구현"  
git commit -m "✨ [feature/#10] AuthController REST API 추가"
git commit -m "✅ [test/#10] 인증 API 테스트 작성"

# 4. PR 생성 및 머지
git push origin feature/#10-user-auth
# GitHub: feature/#10-user-auth → develop PR
```

### **시나리오 2: 긴급 보안 패치**
```bash
# 1. main에서 즉시 분기
git checkout main
git checkout -b hotfix/#15-jwt-security

# 2. 빠른 수정
git commit -m "🐛 [hotfix/#15] JWT 토큰 검증 보안 강화"

# 3. main과 develop 양쪽에 적용
# main으로 즉시 머지
git checkout main
git merge hotfix/#15-jwt-security  
git push origin main

# develop에도 백머지
git checkout develop
git merge hotfix/#15-jwt-security
git push origin develop
```

---

## 💡 팁 & 베스트 프랙티스

### **브랜치 관리**
- 🎯 **하나의 기능 = 하나의 브랜치**
- ⏰ **브랜치 수명 최소화** (1주일 이내)
- 🧹 **정기적인 브랜치 정리** (주 1회)
- 📝 **명확한 브랜치명 사용**

### **커밋 관리**  
- 🔍 **작은 단위로 자주 커밋**
- 📋 **커밋 컨벤션 준수**
- 🎯 **하나의 커밋 = 하나의 목적**

### **PR 관리**
- 📖 **PR 템플릿 활용**
- ✅ **셀프 리뷰 필수**
- 🧪 **테스트 통과 확인**
- 📚 **관련 문서 업데이트**

---

## 🚫 주의사항

### **절대 금지**
- ❌ **main 브랜치 직접 푸시**
- ❌ **force push to shared branches**
- ❌ **브랜치 장기간 방치**
- ❌ **의미 없는 브랜치명**

### **권장하지 않음**
- ⚠️ 여러 기능을 한 브랜치에서 개발
- ⚠️ develop에서 직접 개발
- ⚠️ 테스트 없는 머지
- ⚠️ 브랜치 삭제 없이 방치
