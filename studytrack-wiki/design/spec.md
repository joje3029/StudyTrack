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
| 테스트 | **JUnit 5**, **AssertJ**, **Mockito**, **ArchUnit** | 단위/통합/아키텍처 테스트 |
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

## 🔐 개발 원칙 및 품질 보증 전략

**본 프로젝트는 테스트 주도 개발(TDD)을 핵심 개발 원칙으로 삼습니다.** AI를 활용한 코드 생성을 포함한 모든 기능 구현 및 수정은 반드시 테스트 케이스를 먼저 작성하고, 이를 통과시키는 방식으로 진행합니다. 이는 코드의 안정성과 예측 가능성을 높이고, 의도치 않은 변경을 방지하기 위함입니다.

### 공통 품질 보증 전략
- **TDD (Test-Driven Development)**: 모든 코드는 기능 구현 전 테스트 코드 작성을 원칙으로 합니다.
- **Unit Test 필수 통과**: CI 파이프라인에서 단위/통합 테스트를 실행하며, 하나라도 실패할 경우 PR/PUSH를 차단합니다.
- **테스트 커버리지 80% 유지**: CI에서 커버리지를 측정하며, 전체 커버리지가 80% 미만일 경우 PR/PUSH를 차단합니다.
- **Git Flow 기반 브랜치 전략**: [브랜치 전략 가이드](../collaboration/git-branch-strategy.md) 준수.
- **Lint 필수 통과**: CI 파이프라인에서 Lint 검사를 통과하지 못한 코드는 PR/PUSH를 차단합니다.
- **API 명세 자동화**: Swagger를 통해 API 문서를 항상 최신 상태로 유지합니다.
- **일관된 코드 스타일**: Prettier, Checkstyle 등을 통해 코드 스타일을 통일합니다.

---

## 🏛️ 아키텍처 패턴

본 프로젝트는 **계층형 아키텍처 (Layered Architecture)**를 따릅니다. 이 결정은 1인 사이드 프로젝트의 개발 속도와 생산성을 극대화하기 위한 실용적인 선택입니다.

헥사고날 아키텍처와 같이 프레임워크로부터 비즈니스 로직을 완전히 분리하는 패턴은 유연성이 높지만, 그에 따른 추가적인 복잡성(Port, Adapter 등)이 발생합니다. 현재 프로젝트의 규모와 목적에서는 Spring Boot 프레임워크의 기능을 적극적으로 활용하는 것이 더 효율적이라고 판단했습니다.

### 주요 계층 및 역할

- **Presentation Layer (표현 계층)**: `Controller`가 위치하며, HTTP 요청 처리, 데이터 검증, Service 계층 호출을 담당합니다.
- **Business Layer (비즈니스 계층)**: `Service`가 위치하며, 핵심 비즈니스 로직과 트랜잭션을 처리합니다.
- **Data Access Layer (데이터 접근 계층)**: `Repository`와 `Entity`가 위치하며, 데이터베이스와의 영속성을 담당합니다.
- **DTO (Data Transfer Object)**: 각 계층 간 데이터 전송을 담당하며, 특히 외부로부터 `Entity`를 보호하는 역할을 합니다.

이 아키텍처의 각 계층 간 의존성 규칙은 `ArchUnit` 테스트를 통해 강제됩니다.

---

## 🧪 테스트 커버리지 측정 및 관리

본 프로젝트는 코드 품질을 정량적으로 관리하기 위해 **전체 테스트 커버리지 80% 이상**을 유지하는 것을 원칙으로 합니다.

### 백엔드 (Java/Gradle)

- **도구**: **JaCoCo** (Java Code Coverage) Gradle 플러그인
- **설정**: `build.gradle`의 `plugins`에 `id 'jacoco'`를 추가하고, 관련 태스크를 설정합니다.
- **로컬 실행 및 확인**:
  ```bash
  # 테스트 실행 및 커버리지 리포트 생성
  ./gradlew test jacocoTestReport

  # 리포트 확인: 브라우저에서 아래 파일을 열어 확인
  # studytrack-backend/build/reports/jacoco/test/html/index.html
  ```

### 프론트엔드 (React/Vitest)

- **도구**: **Vitest** 내장 커버리지 (`v8` provider)
- **설정**: `vite.config.ts`의 `test` 객체에 `coverage` 설정을 추가합니다.
- **로컬 실행 및 확인**:
  ```bash
  # package.json의 scripts에 "test:coverage": "vitest run --coverage" 추가
  npm run test:coverage

  # 리포트 확인: 브라우저에서 아래 파일을 열어 확인
  # studytrack-front/coverage/index.html
  ```

### CI 파이프라인 연동

- CI (GitHub Actions 등) 워크플로우에 위 커버리지 측정 명령어를 실행하는 단계를 추가합니다.
- 측정된 커버리지 결과가 80% 미만일 경우, 워크플로우를 실패 처리하여 코드가 병합되지 않도록 강제합니다.
- `Codecov`, `Coveralls`와 같은 서드파티 서비스를 연동하면 PR에 커버리지 변경 사항을 시각적으로 표시해주어 리뷰에 도움이 됩니다.

---

## 🧩 향후 고려 사항 (옵션)

| 영역 | 도구/기술 |
|------|-----------|
| DB 마이그레이션 | Flyway, Liquibase |
| 인증 | JWT, OAuth2 |
| 에러 핸들링 | Global Exception Handler 구성 |
| 배포 자동화 | GitHub Actions, Docker |

---