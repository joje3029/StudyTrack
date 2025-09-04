# 📌 StudyTrack Task 목록 (공통 응답 규격 + 기반 작업 반영)

---

## ✅ 개발 작업 순서 기반 체크리스트

| 순서 | 작업 내용 | 관련 Task |
|------|-----------|------------|
| 1 | PostgreSQL Docker 세팅 및 연결 확인 | T-DB-01 |
| 2 | ERD 확정 → 엔티티/Repository/Service 정의 | T-DB-02 ~ T-DB-04 |
| 3 | Swagger 기반 API 명세서 작성 | T-API-01 |
| 4 | 프론트 퍼블리싱 (기능 없는 UI 화면 제작) | T-FP-01 ~ T-FP-07 |
| 5 | 공통 API 응답 규격 적용 (`api_response_spec.md`) | T-COM-01 |
| 6 | 보안 설정(HTTPS, 보안 헤더, JWT 정책) | T-COM-02 |
| 7 | 백엔드 기능별 개발 + 프론트 연동 | 각 F-01 ~ F-07 Task |

---

## ⚙️ DB 및 API 설계
- [T-DB-01] PostgreSQL Docker Compose 개발 환경 구성 ⭐ High
- [T-DB-02] ERD 기반 엔티티 설계 및 코드 작성 ⭐ High
- [T-DB-03] Repository, Service 기본 골격 작성 ⭐ High
- [T-DB-04] 데이터 초기화 및 예제 시드 구성 ⭐ Medium
- [T-API-01] Swagger 기반 API 문서화 ⭐ High
- [T-COM-01] **공통 API 응답 규격 적용 (guid, resultCode, resultMessage, data)** ⭐ High
- [T-COM-02] HTTPS 적용 및 보안 헤더 설정 ⭐ High

---

## 🔧 퍼블리싱 (기능 없는 UI 기반) : 목업
- [T-FP-01] 전체 라우팅 기반 페이지 컴포넌트 구성 ⭐ High
- [T-FP-02] 로그인/메인/노트/문제 등 기본 뷰 퍼블리싱 ⭐ High
- [T-FP-03] 반응형 및 스타일링 베이스 템플릿 구성 ⭐ Medium
- [T-FP-04] 상태관리 및 Layout 구조 설계 (Recoil/Zustand) ⭐ High
- [T-FP-05] 공통 Header/Footer 및 네비게이션 구성 ⭐ Medium
- [T-FP-06] Button, Card, List 등 공통 UI 컴포넌트 정의 ⭐ Medium
- [T-FP-07] 에러 페이지 및 UX 대응 레이아웃 구성 ⭐ Low

---

## 🔐 F-01 로그인 및 인증
- [T-01-F] 회원가입/로그인 UI ⭐ High
- [T-02-F] 입력값 유효성 검사 ⭐ High
- [T-03-B] 회원가입 API ⭐ High
- [T-04-B] 로그인 API ⭐ High
- [T-05-B] 비밀번호 해싱 및 중복 확인 ⭐ High
- [T-06-F] API 연동 + 에러 핸들링 (공통 응답) ⭐ Medium
- [T-07-F] 소셜 로그인 UI ⭐ High
- [T-08-B] 소셜 로그인 API ⭐ High
- [T-09-B] 소셜 토큰 검증 + JWT 발급 ⭐ High
- [T-10-F] 로그인 UX 처리 공통화 ⭐ Medium
- [T-11-B] JWT 저장/재발급 정책 ⭐ Medium
- [T-12-F] 로그아웃 처리 ⭐ Medium

---

## 🗂️ F-02 학습 분야 관리
- [T-13-F] 분야 CRUD UI ⭐ High
- [T-14-B] 분야 API ⭐ High
- [T-15-F] 검색 필터 ⭐ Medium
- [T-16-F] 연관 노트/문제 표시 ⭐ Medium

---

## 📝 F-03 노트 관리
- [T-17-F] TipTap 에디터 ⭐ High
- [T-18-B] 노트 CRUD API ⭐ High
- [T-19-F] 수동 저장 버튼 ⭐ High
- [T-20-F] 미리보기 ⭐ Low
- [T-21-F] 리스트/정렬 ⭐ Medium
- [T-22-F] 실시간 저장(추후) ⭐ Low

---

## ❓ F-04 문제 생성/풀이
- [T-23-F] 문제 생성 UI ⭐ High
- [T-24-B] 문제 CRUD API ⭐ High
- [T-25-F] 풀이 팝업/타이머 ⭐ High
- [T-26-B] LLM 채점 API ⭐ Medium
- [T-27-B] 결과 저장 ⭐ High
- [T-28-B] 오답 자동 저장 ⭐ High

---

## 🔴 F-05 오답노트 관리
- [T-29-B] 오답노트 API ⭐ High
- [T-30-F] 오답 필터 UI ⭐ Medium
- [T-31-F] 재풀이 UI ⭐ Medium
- [T-32-F] 채점 히스토리 UI ⭐ Medium

---

## 🧠 F-06 암기 세션 모드
- [T-33-F] 암기 카드 UI ⭐ Medium
- [T-34-B] 세션 결과 저장 API ⭐ Medium
- [T-35-F] 요약/히트맵 UI ⭐ Medium
- [T-36-F] 무작위/반복 옵션 UI ⭐ Low

---

## 📅 F-07 학습 캘린더
- [T-37-F] 목표 입력 UI ⭐ High
- [T-38-B] 목표 저장 API ⭐ Medium
- [T-39-F] 목표 수정 UI ⭐ Medium
- [T-40-F] 반복 일정 기능 ⭐ Low

---

## 📌 추후 고려
- [T-P01] 노트 실시간 저장
- [T-P02] AI 리마인더
- [T-P03] 문제 자동 출제 / PDF 출력
- [T-P04] 오답 복습 스케줄링
