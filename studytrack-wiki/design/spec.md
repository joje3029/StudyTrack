# 🧠 StudyTrack 기술 스택

**StudyTrack**은 개인 학습 기록과 문제 풀이를 위한 웹 애플리케이션입니다.  
사용자 중심의 UI/UX, 효율적인 백엔드 구조, 테스트 기반 개발(TDD)을 목표로 구성합니다.

---

## ⚛️ 프론트엔드 스택

| 항목 | 선택 기술 | 비고 |
|------|-----------|------|
| 프레임워크 | **React** + **TypeScript** | 유지보수 쉬운 구조 |
| 빌드 도구 | **Vite** | Webpack보다 빠르고 SPA에 적합 |
| 스타일 | **Tailwind CSS** | 유틸리티 기반 스타일 시스템 |
| UI 컴포넌트 | **shadcn/ui** | 접근성 우수하고 모던한 UI 컴포넌트 |
| 상태 관리 | **Zustand**, **TanStack Query** | 전역 상태 + 서버 상태 효율적 분리 |
| 테스트 | **React Testing Library**, **Vitest** | TDD 중심 유닛/통합 테스트 |
| 문서화 | **Storybook** | 컴포넌트 단위 문서 및 개발 |
| 코드 품질 | **ESLint**, **Prettier** | 일관된 코드 스타일 |
| E2E 테스트 | **Cypress** _(선택)_ | 사용자 시나리오 테스트 |
| CI | ✅ Lint 실패 시 PR/PUSH 제한 | 코드 규칙 강제 적용 |

---

## ☕ 백엔드 스택

| 항목 | 선택 기술 | 비고 |
|------|-----------|------|
| 언어 | **Java 21** | 안정적이고 성숙한 생태계 |
| JDK 버전 | **Java 21** | 최신 기능 및 성능 개선 활용 |
| 빌드 도구 | **Gradle** | 유연한 빌드 설정 |
| 프레임워크 | **Spring Boot 3.5.4** | 최신 Spring 기술 기반 |
| 아티팩트 타입 | **JAR** | 독립 실행형 애플리케이션 |
| 주요 모듈 | `spring-boot-starter-web` <br> `spring-boot-starter-security` <br> `spring-boot-starter-data-jpa` | REST API, 인증/인가, DB 연동 |
| DB | **H2** (로컬/테스트), **PostgreSQL** (개발/운영) | 환경별 DB 분리 |
| API 문서화 | **Swagger UI** (`springdoc-openapi`) | 자동 API 명세 및 테스트 UI 제공 |
| 로깅 | **SLF4J** + **Logback** | 표준 Java 로깅 프레임워크 |
| 코드 품질 | **Checkstyle**, **SpotBugs** | 정적 분석 및 스타일 검사 |
| CI | ✅ Lint 실패 시 PR/PUSH 제한 | 코드 품질 강제 유지 |

---

## 📁 현재 프로젝트 구조
StudyTrack/
├── studytrack-front/ # 프론트엔드 (React + TypeScript)
│ ├── src/
│ ├── .eslintrc.json
│ ├── .prettierrc
│ └── vite.config.ts
├── studytrack-backend/ # 백엔드 (Spring Boot + Java 21)
│ ├── src/main/java/
│ ├── src/test/java/
│ ├── src/main/resources/ # 환경별 설정 파일
│ ├── build.gradle
│ ├── config/checkstyle/
│ └── .editorconfig
├── studytrack-wiki/ # 프로젝트 문서
└── studytrack-erd/ # 데이터베이스 설계

---

## 🌍 환경별 설정

### 데이터베이스 설정
- **로컬 개발**: H2 인메모리 DB (별도 설치 불필요)
- **테스트**: H2 인메모리 DB
- **개발 서버**: PostgreSQL (`studytrack_dev`)
- **운영 서버**: PostgreSQL (환경변수 기반)

### 프로파일 설정
- `application-local.yml`: 로컬 개발 환경
- `application-dev.yml`: 개발 서버 환경  
- `application-prod.yml`: 운영 환경
- `application-test.yml`: 테스트 환경

### 실행 방법
```bash
# 로컬 개발 (기본값)
./gradlew bootRun

# 개발 서버
./gradlew bootRun --args='--spring.profiles.active=dev'

# H2 콘솔: http://localhost:8080/h2-console
```

---

## 🔐 품질 보증 전략 (Front & Back 공통)

- **Git Flow 기반 브랜치 전략** → [브랜치 전략 가이드](./git-branch-strategy.md)
- **Lint 필수 통과 → CI에서 PR/PUSH 차단**
- **TDD 기반 테스트 개발**
- **API 명세는 Swagger로 자동화**  
- **코드 스타일은 팀/개인 기준에 따라 통일 관리**

---

## 🧩 향후 고려 사항 (옵션)

| 영역 | 도구/기술 |
|------|-----------|
| DB 마이그레이션 | Flyway, Liquibase |
| 인증 | JWT, OAuth2 |
| 에러 핸들링 | Global Exception Handler 구성 |
| 배포 자동화 | GitHub Actions, Docker |

---