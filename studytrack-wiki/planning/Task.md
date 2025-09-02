# 📌 StudyTrack Task 목록 (v0.2 - 작업 순서 기반)

## ✅ 개발 작업 순서 기반 체크리스트

| 순서 | 작업 내용 | 관련 Task |
|------|-----------|------------|
| 1 | PostgreSQL Docker 세팅 및 연결 확인 | T-DB-01 |
| 2 | ERD 확정 → 엔티티/Repository/Service 정의 | T-DB-02 ~ T-DB-04 |
| 3 | Swagger 기반 API 명세서 작성 | T-36-B |
| 4 | 프론트 퍼블리싱 (기능 없는 UI 화면 제작) | T-FP-01 ~ T-FP-07 |
| 5 | 백엔드 기능별 개발 + 프론트 연동 | 아래 각 F-01 ~ F-07 Task |

---

## ⚙️ DB 및 API 설계
- [T-DB-01] PostgreSQL Docker Compose 개발 환경 구성 ⭐ High (v) - 2025.07.26
- [T-DB-02] ERD 기반 엔티티 설계 및 코드 작성 ⭐ High (v) - 2025.07.26
- [T-DB-03] Repository, Service 기본 골격 작성 ⭐ High (v) - 2025.07.26
- [T-DB-04] 데이터 초기화 및 예제 시드 구성 ⭐ Medium - 2025.07.26 : DB에가 데이터까지 함.

---

## 🔧 퍼블리싱 (기능 없는 UI 기반)
- [T-FP-01] 전체 라우팅 기반 페이지 컴포넌트 구성 ⭐ High
- [T-FP-02] 로그인/메인/노트/문제 등 기본 뷰 퍼블리싱 ⭐ High
- [T-FP-03] 반응형 및 스타일링 베이스 템플릿 구성 ⭐ Medium
- [T-FP-04] 상태관리 및 Layout 구조 설계 (Recoil/Zustand) ⭐ High
- [T-FP-05] 공통 Header/Footer 및 네비게이션 구성 ⭐ Medium
- [T-FP-06] Button, Card, List 등 공통 UI 컴포넌트 정의 ⭐ Medium
- [T-FP-07] 에러 페이지 및 UX 대응 레이아웃 구성 ⭐ Low

---

## 🔐 F-01 인증 (로그인 및 회원가입)

### 자체 인증 (Email/Password)
- [T-01-F] 자체 회원가입/로그인 페이지 UI 구성 ⭐ High
- [T-02-F] 이메일/비밀번호 유효성 검사 로직 구현 ⭐ High
- [T-03-B] 자체 회원가입 API (`/auth/register`) 구현 ⭐ High
- [T-04-B] 자체 로그인 API (`/auth/login`) 구현 ⭐ High
- [T-05-B] 비밀번호 암호화(BCrypt) 및 중복 확인 로직 ⭐ High
- [T-06-F] 회원가입/로그인 API 연동 및 에러 핸들링 ⭐ Medium

### 소셜 로그인 (OAuth2)
- [T-07-F] 소셜 로그인 버튼 UI (Google, Naver, Kakao) 구성 ⭐ High
- [T-08-B] OAuth 인증 처리 API (`/auth/social-login`) 구성 ⭐ High
- [T-09-B] 액세스 토큰 유효성 검증 및 JWT 발급 공통 로직 ⭐ High
- [T-10-F] 로그인 성공/실패 UX 처리 공통화 ⭐ Medium

### 공통
- [T-11-B] JWT 저장 정책 수립 및 Refresh Token 로직 구현 ⭐ Medium
- [T-12-F] 로그인 상태 유지 및 로그아웃 처리 ⭐ Medium

---

## 🗂️ F-02 학습 분야 관리
- [T-13-F] 학습 분야 리스트 및 CRUD UI ⭐ High
- [T-14-B] 분야 API (생성/수정/삭제/조회) ⭐ High
- [T-15-F] 분야 실시간 검색 필터 기능 구현 ⭐ Medium
- [T-16-F] 선택된 분야에 따른 연관 노트 및 문제 표시 ⭐ Medium

---

## 📝 F-03 노트 관리 (TipTap Editor)
- [T-17-F] TipTap 에디터 컴포넌트 구현 ⭐ High
- [T-18-B] 노트 CRUD API 구현 ⭐ High
- [T-19-F] 저장 버튼 및 수동 저장 처리 UX ⭐ High
- [T-20-F] 마크다운 → HTML 미리보기 기능 ⭐ Low
- [T-21-F] 노트 리스트 화면 및 최근순 정렬 ⭐ Medium
- [T-22-F] 실시간 저장 기능 (추후) ⭐ Low

---

## ❓ F-04 문제 생성/풀이
- [T-23-F] 문제 생성 UI (객관식/주관식/단답형) ⭐ High
- [T-24-B] 문제 저장/조회 API 구현 ⭐ High
- [T-25-F] 문제 풀이 팝업 + 타이머 기능 ⭐ High
- [T-26-B] LLM 채점 API 구성 및 통신 처리 ⭐ Medium
- [T-27-B] 문제풀이 결과 저장 처리 ⭐ High
- [T-28-B] 오답 자동 저장 처리 (WrongNote 연결) ⭐ High

---

## 🔴 F-05 오답노트 관리
- [T-29-B] 오답노트 API 구조 및 저장 처리 ⭐ High
- [T-30-F] 틀린/북마크 문제 필터 UI 구현 ⭐ Medium
- [T-31-F] 오답노트 문제 다시 풀기 UI ⭐ Medium
- [T-32-F] LLM 채점 결과 히스토리/시각화 ⭐ Medium

---

## 🧠 F-06 암기 세션 모드
- [T-33-F] 암기 카드 UI (플립/문제형 전환) ⭐ Medium
- [T-34-B] 암기 결과 저장 처리 ⭐ Medium
- [T-35-F] 암기 결과 요약/히트맵 화면 ⭐ Medium
- [T-36-F] 세션 무작위/반복 설정 UI ⭐ Low

---

## 📅 F-07 학습 캘린더
- [T-37-F] 날짜별 목표 입력 UI ⭐ High
- [T-38-B] 목표 상태 저장 및 미체크 → 미완료 처리 ⭐ Medium
- [T-39-F] 과거 목표 수정 가능 처리 ⭐ Medium
- [T-40-F] 반복 일정 기능 (추후 고려) ⭐ Low

---

## 📌 추후 고려 사항 (Pending)
- [T-P01] 노트 실시간 저장 (Auto Save + debounce)
- [T-P02] AI 기반 리마인더 기능 (목표 추천)
- [T-P03] 문제 자동 출제 / 문제지 PDF 출력
- [T-P04] 오답 자동 복습 스케줄링