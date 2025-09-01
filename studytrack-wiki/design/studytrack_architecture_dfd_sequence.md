# 📦 StudyTrack 프로젝트 다이어그램 정리 (Architecture, DFD, Sequence)

---

## 🏗️ 시스템 아키텍처 다이어그램

### ✅ Mermaid 아키텍처 다이어그램

```mermaid
graph TD
    subgraph 사용자
        A[🧑 사용자 브라우저]
    end

    subgraph 프론트엔드 (React + Vite)
        B[🌐 React App<br/>(Vite + Zustand + Tailwind)]
    end

    subgraph 백엔드 (Kotlin + Spring Boot)
        C[🚀 Spring Boot API 서버<br/>(Kotlin, JPA, Swagger)]
        D[⚡ FastAPI LLM 서버<br/> (문제 채점 전용)]
    end

    subgraph 데이터베이스 (Docker)
        E[(🛢️ PostgreSQL DB)]
    end

    A --> B
    B -->|REST API 요청| C
    C -->|JPA/SQL 처리| E
    C -->|LLM 채점 요청| D
    D -->|채점 결과 응답| C
```

---

## 📘 아키텍처 설명

- 프론트엔드: React + Vite + Zustand + Tailwind
- 백엔드: Kotlin + Spring Boot + JPA + Swagger
- LLM 서버: FastAPI (주관식/단답형 문제 채점용)
- DB: PostgreSQL (Docker 기반)

---

# 🔄 DFD: 데이터 흐름 다이어그램

## 1️⃣ 문제 풀이 흐름 DFD

```mermaid
flowchart TD
    User[🧑 사용자]
    FE[🌐 프론트엔드<br/>React App]
    BE[🚀 백엔드<br/>Spring Boot]
    DB[(🛢️ PostgreSQL)]
    LLM[⚡ LLM 채점 서버<br/>FastAPI]

    User -->|문제 선택 요청| FE
    FE -->|문제 리스트 요청| BE
    BE -->|문제 조회| DB
    DB -->|문제 목록 반환| BE
    BE --> FE
    FE -->|풀이 제출| BE
    BE -->|주관식/단답형이면 채점 요청| LLM
    LLM -->|채점 결과| BE
    BE -->|풀이 결과 저장| DB
    BE -->|틀린 문제 오답노트 저장| DB
    BE -->|결과 응답| FE
    FE -->|결과 표시| User
```

## 2️⃣ 노트 작성 흐름 DFD

```mermaid
flowchart TD
    User[🧑 사용자]
    FE[🌐 프론트엔드<br/>React App]
    BE[🚀 백엔드<br/>Spring Boot]
    DB[(🛢️ PostgreSQL)]

    User -->|노트 추가 버튼 클릭| FE
    FE -->|에디터 화면 렌더링| User
    User -->|노트 입력 및 저장 클릭| FE
    FE -->|POST /notes 요청| BE
    BE -->|노트 저장| DB
    DB -->|저장 성공| BE
    BE -->|응답 반환 (noteId 등)| FE
    FE -->|노트 리스트 또는 편집 화면 전환| User
```

## 3️⃣ 소셜 로그인 흐름 DFD

```mermaid
flowchart TD
    User[🧑 사용자]
    FE[🌐 프론트엔드<br/>React App]
    OAuth[🔑 OAuth 서버<br/>(Google/Naver/Kakao)]
    BE[🚀 백엔드<br/>Spring Boot]
    DB[(🛢️ PostgreSQL)]

    User -->|소셜 로그인 버튼 클릭| FE
    FE -->|OAuth 인증 요청| OAuth
    OAuth -->|액세스 토큰 전달| FE
    FE -->|POST /auth/oauth-login<br/>+ 액세스 토큰| BE
    BE -->|토큰 검증 및 사용자 조회| OAuth
    BE -->|신규 사용자면 저장| DB
    DB -->|저장 확인| BE
    BE -->|JWT 발급 + 사용자 정보 반환| FE
    FE -->|JWT 저장 후 메인 페이지 이동| User
```

---

# ⏱️ 시퀀스 다이어그램

## 1️⃣ 소셜 로그인

```mermaid
sequenceDiagram
    participant User as 사용자
    participant FE as 프론트엔드 (React)
    participant OAuth as OAuth 서버 (Google/Naver/Kakao)
    participant BE as 백엔드 (Spring Boot)
    participant DB as DB

    User->>FE: 소셜 로그인 버튼 클릭
    FE->>OAuth: 인증 요청 (Redirect)
    OAuth-->>FE: 인증 완료 → 액세스 토큰
    FE->>BE: 액세스 토큰 전달 (/auth/oauth-login)
    BE->>OAuth: 액세스 토큰 검증
    OAuth-->>BE: 사용자 정보 반환
    BE->>DB: 신규 사용자 여부 확인 및 저장
    DB-->>BE: 저장 확인
    BE-->>FE: JWT + 사용자 정보 응답
    FE->>User: JWT 저장 및 메인 페이지 이동
```

## 2️⃣ 노트 작성

```mermaid
sequenceDiagram
    participant User as 사용자
    participant FE as 프론트엔드 (React)
    participant BE as 백엔드 (Spring Boot)
    participant DB as DB

    User->>FE: 노트 추가 버튼 클릭
    FE->>User: 에디터 화면 표시
    User->>FE: 제목/내용 입력 + 저장 클릭
    FE->>BE: POST /notes (노트 저장 요청)
    BE->>DB: 노트 저장
    DB-->>BE: 저장 결과 반환
    BE-->>FE: 노트 ID 등 응답
    FE->>User: 노트 리스트 화면 또는 편집 완료 표시
```

## 3️⃣ 문제 풀이 + LLM 채점

```mermaid
sequenceDiagram
    participant User as 사용자
    participant FE as 프론트엔드 (React)
    participant BE as 백엔드 (Spring Boot)
    participant DB as DB
    participant LLM as LLM 채점 서버 (FastAPI)

    User->>FE: 문제 풀이 시작
    FE->>BE: GET /subjects/{id}/problems
    BE->>DB: 문제 목록 조회
    DB-->>BE: 문제 리스트 반환
    BE-->>FE: 문제 데이터 응답
    FE->>User: 문제 표시 및 입력
    User->>FE: 제출 클릭
    FE->>BE: POST /solve (풀이 제출)
    BE->>LLM: 주관식/단답형 문제 채점 요청
    LLM-->>BE: 채점 결과 (정오, 유사도 등)
    BE->>DB: 결과 저장 (정답 여부, 오답노트 반영)
    DB-->>BE: 저장 완료
    BE-->>FE: 풀이 결과 응답
    FE->>User: 결과 표시 (정답, 해설, 오답 여부 등)
```

---
