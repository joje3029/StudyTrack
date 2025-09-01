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
| 프레임워크 | **Spring Boot 3.2.5** | 최신 Spring 기술 기반 |
| 아티팩트 타입 | **JAR** | 독립 실행형 애플리케이션 |
| 주요 모듈 | `spring-boot-starter-web` <br> `spring-boot-starter-security` <br> `spring-boot-starter-data-jpa` | REST API, 인증/인가, DB 연동 |
| DB | **관계형 데이터베이스** (e.g. PostgreSQL, MySQL) | Spring Data JPA 활용 |
| API 문서화 | **Swagger UI** (`springdoc-openapi`) | 자동 API 명세 및 테스트 UI 제공 |
| 로깅 | **SLF4J** + **Logback** | 표준 Java 로깅 프레임워크 |
| 코드 품질 | **Checkstyle**, **SpotBugs** | 정적 분석 및 스타일 검사 |
| CI | ✅ Lint 실패 시 PR/PUSH 제한 | 코드 품질 강제 유지 |

---

## 📁 폴더 구조 제안 (예시) : 추후 수정 필요
studytrack/
├── front/ # 프론트엔드 (React)
│ ├── src/
│ ├── .eslintrc.json
│ ├── .prettierrc
│ └── vite.config.ts
├── back/ # 백엔드 (Spring Boot)
│ ├── src/main/kotlin/
│ ├── src/test/kotlin/
│ ├── build.gradle.kts
│ ├── .editorconfig
│ └── detekt.yml
└── README.md

---

## 🔐 품질 보증 전략 (Front & Back 공통)

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