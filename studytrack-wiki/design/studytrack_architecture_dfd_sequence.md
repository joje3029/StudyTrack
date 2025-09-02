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

    subgraph 백엔드 (Java + Spring Boot)
        C[🚀 Spring Boot API 서버<br/>(Java, JPA, Swagger)]
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
- 백엔드: **Java 21** + Spring Boot + JPA + Swagger
- LLM 서버: FastAPI (주관식/단답형 문제 채점용)
- DB: PostgreSQL (Docker 기반)

---

# 🔄 DFD: 데이터 흐름 다이어그램

## 1️⃣ 자체 회원가입 및 로그인 흐름 DFD (신규)

```mermaid
flowchart TD
    User[🧑 사용자]
    FE[🌐 프론트엔드<br/>React App]
    BE[🚀 백엔드<br/>Spring Boot]
    DB[(🛢️ PostgreSQL)]

    subgraph 회원가입
        User -- 회원가입 정보 입력 --> FE
        FE -- POST /auth/register --> BE
        BE -- 이메일/닉네임 중복 확인 --> DB
        BE -- 비밀번호 해싱 후 사용자 저장 --> DB
        DB -- 저장 성공 --> BE
        BE -- 회원가입 성공 응답 --> FE
        FE -- 로그인 페이지로 안내 --> User
    end

    subgraph 로그인
        User -- 이메일/비밀번호 입력 --> FE
        FE -- POST /auth/login --> BE
        BE -- 사용자 조회 및 비밀번호 검증 --> DB
        BE -- 인증 성공 시 JWT 발급 --> FE
        FE -- JWT 저장 후 메인 페이지 이동 --> User
    end
```

## 2️⃣ 소셜 로그인 흐름 DFD

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
    FE -->|POST /auth/social-login<br/>+ 액세스 토큰| BE
    BE -->|토큰 검증 및 사용자 조회| OAuth
    BE -->|신규 사용자면 저장| DB
    DB -->|저장 확인| BE
    BE -->|JWT 발급 + 사용자 정보 반환| FE
    FE -->|JWT 저장 후 메인 페이지 이동| User
```

---

# ⏱️ 시퀀스 다이어그램

## 1️⃣ 자체 로그인 (신규)

```mermaid
sequenceDiagram
    participant User as 사용자
    participant FE as 프론트엔드 (React)
    participant BE as 백엔드 (Spring Boot)
    participant DB as DB

    User->>FE: 이메일, 비밀번호 입력 후 로그인 요청
    FE->>BE: POST /auth/login
    BE->>DB: 사용자 정보 조회 (암호화된 비밀번호 포함)
    DB-->>BE: 사용자 정보 반환
    BE->>BE: 제출된 비밀번호와 DB의 해시 비교
    alt 인증 성공
        BE-->>FE: JWT (Access/Refresh Token) 발급
        FE->>FE: 토큰 저장 (HttpOnly Cookie 등)
        FE->>User: 로그인 성공 및 메인 페이지 이동
    else 인증 실패
        BE-->>FE: 401 Unauthorized 에러 응답
        FE->>User: 로그인 실패 메시지 표시
    end
```

## 2️⃣ 소셜 로그인

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
    FE->>BE: 액세스 토큰 전달 (/auth/social-login)
    BE->>OAuth: 액세스 토큰 검증
    OAuth-->>BE: 사용자 정보 반환
    BE->>DB: 신규 사용자 여부 확인 및 저장
    DB-->>BE: 저장 확인
    BE-->>FE: JWT + 사용자 정보 응답
    FE->>User: JWT 저장 및 메인 페이지 이동
```