# 🛡️ GitHub Branch Protection 설정 가이드

이 문서는 StudyTrack 프로젝트의 GitHub Repository에서 Branch Protection 규칙을 설정하는 방법을 안내합니다.

---

## 🎯 설정 목적

- **main/develop 브랜치 보호**: 직접 푸시 방지
- **코드 품질 보장**: CI 테스트 통과 강제
- **코드 리뷰 강제**: Pull Request를 통한 코드 검토
- **커버리지 기준 준수**: 테스트 커버리지 최소 기준 유지

---

## ⚙️ Branch Protection 규칙 설정

### 1. GitHub Repository 설정 접근

1. **GitHub Repository** → **Settings** → **Branches** 이동
2. **Add rule** 또는 기존 규칙 **Edit** 클릭

### 2. main 브랜치 보호 규칙

**Branch name pattern**: `main`

#### ✅ 필수 설정
- [ ] **Restrict pushes that create files larger than 100 MB**
- [x] **Require a pull request before merging**
  - [x] **Require approvals**: 1명 이상
  - [x] **Dismiss stale PR approvals when new commits are pushed**
  - [x] **Require review from code owners** (CODEOWNERS 파일 있을 경우)
- [x] **Require status checks to pass before merging**
  - [x] **Require branches to be up to date before merging**
  - **Status checks**: 다음 항목들을 추가
    - `Backend CI / test`
    - `Frontend CI / test`
- [x] **Require conversation resolution before merging**
- [x] **Require signed commits** (선택사항)
- [x] **Require linear history** (선택사항)
- [x] **Include administrators** (관리자도 규칙 적용)

### 3. develop 브랜치 보호 규칙

**Branch name pattern**: `develop`

#### ✅ 필수 설정 (main과 동일하되 일부 완화)
- [x] **Require a pull request before merging**
  - [x] **Require approvals**: 1명 이상
  - [x] **Dismiss stale PR approvals when new commits are pushed**
- [x] **Require status checks to pass before merging**
  - [x] **Require branches to be up to date before merging**
  - **Status checks**: 
    - `Backend CI / test`
    - `Frontend CI / test`
- [x] **Require conversation resolution before merging**
- [x] **Include administrators**

---

## 📋 Status Checks 상세 설정

### Backend CI 체크리스트
- ✅ **Java 21 컴파일 성공**
- ✅ **Checkstyle 통과**
- ✅ **SpotBugs 분석 통과**
- ✅ **단위 테스트 통과**
- ✅ **테스트 커버리지 70% 이상**

### Frontend CI 체크리스트
- ✅ **TypeScript 컴파일 성공**
- ✅ **ESLint 통과**
- ✅ **Prettier 포맷 검사 통과**
- ✅ **단위 테스트 통과**
- ✅ **테스트 커버리지 70% 이상**
- ✅ **빌드 성공**

---

## 🔧 CODEOWNERS 파일 설정 (선택사항)

Repository 루트에 `.github/CODEOWNERS` 파일을 생성하여 코드 소유자를 지정할 수 있습니다.

```
# Global owners
* @your-github-username

# Backend specific
studytrack-backend/ @backend-team-member

# Frontend specific  
studytrack-front/ @frontend-team-member

# CI/CD workflows
.github/workflows/ @devops-team-member

# Documentation
*.md @documentation-team-member
studytrack-wiki/ @documentation-team-member
```

---

## 🚨 Emergency 상황 대응

### Hotfix 프로세스
1. **main에서 hotfix 브랜치 생성**
   ```bash
   git checkout main
   git pull origin main
   git checkout -b hotfix/critical-bug-fix
   ```

2. **긴급 수정 후 PR 생성**
   - Target: `main` 브랜치
   - 리뷰어: 최소 1명 지정
   - CI 통과 확인

3. **main 머지 후 develop에도 반영**
   ```bash
   git checkout develop
   git merge main
   git push origin develop
   ```

### 규칙 일시 해제
- **Settings** → **Branches** → 해당 규칙 **Edit**
- 긴급 상황 해결 후 **즉시 재활성화**

---

## ✅ 설정 확인 체크리스트

### Branch Protection 확인
- [ ] main 브랜치 직접 푸시 차단 확인
- [ ] develop 브랜치 직접 푸시 차단 확인
- [ ] PR 없이 머지 시도 시 차단 확인

### CI/CD 통합 확인
- [ ] PR 생성 시 자동으로 CI 실행 확인
- [ ] CI 실패 시 머지 차단 확인
- [ ] 테스트 커버리지 미달 시 차단 확인

### 알림 설정 확인
- [ ] PR 생성 시 팀원들에게 알림 전송 확인
- [ ] CI 실패 시 작성자에게 알림 전송 확인
- [ ] 머지 완료 시 관련자들에게 알림 전송 확인

---

## 🎯 모범 사례

### PR 작성 시
1. **명확한 제목과 설명** 작성
2. **관련 이슈 번호** 연결
3. **스크린샷/GIF** 첨부 (UI 변경 시)
4. **테스트 시나리오** 명시
5. **Breaking Changes** 여부 명시

### 코드 리뷰 시
1. **건설적인 피드백** 제공
2. **코딩 컨벤션** 준수 확인
3. **테스트 코드** 품질 검토
4. **성능 및 보안** 고려사항 검토
5. **문서화** 필요성 검토

### 머지 후
1. **기능 브랜치 삭제**
2. **배포 상태 모니터링**
3. **이슈 상태 업데이트**
4. **다음 작업 계획** 수립

---

## 📚 참고 자료

- [GitHub Branch Protection 공식 문서](https://docs.github.com/en/repositories/configuring-branches-and-merges-in-your-repository/managing-protected-branches/about-protected-branches)
- [CODEOWNERS 설정 가이드](https://docs.github.com/en/repositories/managing-your-repositorys-settings-and-features/customizing-your-repository/about-code-owners)
- [GitHub Actions Status Checks](https://docs.github.com/en/pull-requests/collaborating-with-pull-requests/collaborating-on-repositories-with-code-quality-features/about-status-checks)
