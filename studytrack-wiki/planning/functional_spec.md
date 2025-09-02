## F-01 로그인 및 인증

### 프로젝트 나눔
- Core 기능 (필수/초기 기능)
    - **로그인 (자체/소셜)**
    - 분야/노트 관리
    - 문제 관리
    - 암기 세션
    - 캘린더 관리

- Infra & 공통 요소
    - 상태 관리 구조 설정 (Recoil/Zustand)
    - Editor 구성 (tiptap 세팅)
    - LLM 연동 구조 설계 (주관식/단답형 채점)
    - 백엔드 API 명세 작성
    - Swagger 적용
    - CI/CD (Lint + Push 제한)

- 추후 고려 Task (F-기능의 "추후 고려" 반영)
    - 실시간 저장
    - 반복 학습 일정
    - AI 리마인더

### 1. 개요
- **자체 로그인**과 **소셜 로그인(Google, Naver, Kakao)**을 모두 지원합니다.
- 모든 인증 방식은 최종적으로 서버에서 **JWT(JSON Web Token)**를 발급받아 사용합니다.
- 인증 관련 모든 API 통신은 **HTTPS**를 통해 암호화되어야 합니다.

### 2. 자체 로그인 (신규 추가)

#### 주요 흐름
1.  **회원가입**:
    1.  사용자는 아이디(이메일), 비밀번호, 닉네임을 입력하여 회원가입을 요청합니다.
    2.  서버는 아이디(이메일) 중복 여부를 확인합니다.
    3.  비밀번호는 **BCrypt** 또는 **Argon2** 해시 함수로 암호화하여 데이터베이스에 저장합니다.
    4.  회원가입 성공 시, 사용자에게 성공 메시지를 반환합니다.
2.  **로그인**:
    1.  사용자는 아이디(이메일)와 비밀번호를 입력하여 로그인을 요청합니다.
    2.  서버는 사용자 존재 여부를 확인하고, 제출된 비밀번호를 저장된 해시와 비교합니다.
    3.  인증 성공 시, **Access Token**과 **Refresh Token**을 JWT 형식으로 발급합니다.
    4.  클라이언트는 토큰을 안전한 곳(HttpOnly Cookie 또는 Secure Storage)에 저장하고 로그인 상태를 유지합니다.

### 3. 소셜 로그인

#### 주요 흐름
1.  사용자가 로그인 화면에서 원하는 소셜 로그인 버튼을 클릭합니다.
2.  OAuth 2.0 인증 절차를 통해 각 소셜 플랫폼으로부터 **Access Token**을 발급받습니다.
3.  클라이언트는 발급받은 소셜 Access Token을 서버에 전달합니다.
4.  서버는 해당 토큰을 검증하고 사용자 정보를 조회합니다.
    -   기존 사용자가 아닌 경우, 해당 소셜 정보로 신규 회원가입을 자동 처리합니다.
5.  인증 성공 시, 서버는 자체 **Access Token**과 **Refresh Token**을 JWT 형식으로 발급합니다.
6.  클라이언트는 토큰을 저장하고 로그인 상태를 유지합니다.

### 4. 핵심 보안 요구사항

| 항목 | 내용 | 필수 구현 사항 |
| :--- | :--- | :--- |
| **비밀번호 저장** | 사용자의 비밀번호는 절대 평문으로 저장해서는 안 되며, 복호화가 불가능한 단방향 해시 함수를 사용해야 합니다. | - **BCrypt** 또는 **Argon2** 사용<br>- 사용자별로 고유한 Salt를 적용하여 Rainbow Table 공격 방지 |
| **인증 토큰** | 상태 비저장(Stateless) 인증을 위해 JWT를 사용하며, 토큰 탈취에 대비한 정책이 필요합니다. | - Access Token은 만료 시간을 짧게(예: 15분~1시간) 설정<br>- Refresh Token은 만료 시간을 길게(예: 7일~30일) 설정하고, 안전한 저장소(DB 등)에 보관<br>- Refresh Token을 사용하여 Access Token을 재발급하는 로직 구현 |
| **전송 계층 보안** | 클라이언트와 서버 간의 모든 통신은 암호화되어야 합니다. | - **HTTPS/TLS** 적용 필수 |
| **로그인 시도 제한** | Brute-force 및 무차별 대입 공격을 방지하기 위한 보호 조치가 필요합니다. | - 특정 시간 동안 일정 횟수 이상 로그인 실패 시 계정 잠금(예: 5회 실패 시 5분간 잠금)<br>- IP 기반으로도 요청 횟수를 제한하여 비정상적인 트래픽 차단 |
| **보안 헤더** | XSS, CSRF 등 웹 취약점 공격을 방지하기 위해 HTTP 응답 헤더를 설정해야 합니다. | - `X-Content-Type-Options: nosniff`<br>- `X-Frame-Options: deny`<br>- `Content-Security-Policy` (CSP) 설정 |
| **세션/토큰 관리** | 로그아웃 및 토큰 만료 시 명확한 처리가 필요합니다. | - 로그아웃 시 서버에서 Refresh Token을 무효화 처리<br>- 클라이언트 측에서도 저장된 토큰을 즉시 삭제 |

### 5. API 상세 명세

#### `POST /api/v1/auth/register` (자체 회원가입)
- **설명**: 이메일, 비밀번호, 닉네임을 사용하여 신규 사용자를 등록합니다.
- **인증**: 필요 없음

**Request Body**
| 필드 | 타입 | 제약조건 | 설명 |
|---|---|---|---|
| `email` | String | `Not Null`, `Email Format` | 로그인 시 사용할 이메일 주소 |
| `password` | String | `Not Null`, `Min 8 chars` | 비밀번호 (영문, 숫자, 특수문자 조합) |
| `nickname` | String | `Not Null`, `Max 50` | 사용자 닉네임 |

**Success Response (`201 Created`)**
- Body 없음.

**Error Responses**
| Status Code | errorCode | 설명 |
|---|---|---|
| `400 Bad Request` | `INVALID_INPUT` | 이메일, 비밀번호, 닉네임 형식이 유효하지 않음 |
| `409 Conflict` | `EMAIL_ALREADY_EXISTS` | 이미 가입된 이메일 |
| `409 Conflict` | `NICKNAME_ALREADY_EXISTS` | 이미 사용 중인 닉네임 |

---

#### `POST /api/v1/auth/login` (자체 로그인)
- **설명**: 이메일과 비밀번호로 사용자를 인증하고 JWT를 발급합니다.
- **인증**: 필요 없음

**Request Body**
| 필드 | 타입 | 제약조건 | 설명 |
|---|---|---|---|
| `email` | String | `Not Null`, `Email Format` | 가입된 이메일 주소 |
| `password` | String | `Not Null` | 비밀번호 |

**Success Response (`200 OK`)**
| 필드 | 타입 | 설명 |
|---|---|---|
| `accessToken` | String | 서버가 발급한 JWT Access Token |
| `refreshToken` | String | Access Token 재발급에 사용될 Refresh Token |
| `user` | Object | 로그인한 사용자 정보 |

---

#### `POST /api/v1/auth/social-login` (소셜 로그인)
- **설명**: 소셜 플랫폼의 Access Token을 받아 사용자를 인증하고 JWT를 발급합니다.
- **인증**: 필요 없음

**Request Body**
| 필드 | 타입 | 제약조건 | 설명 |
|---|---|---|---|
| `provider` | String | `Enum` ("google", "kakao", "naver") | OAuth 제공자 |
| `providerAccessToken` | String | `Not Null` | 소셜 플랫폼에서 발급받은 Access Token |

**Success Response (`200 OK`)**
- 자체 로그인 성공 응답과 동일한 구조를 가집니다.

---

#### `POST /api/v1/auth/refresh` (토큰 재발급)
- **설명**: 유효한 Refresh Token을 사용하여 만료된 Access Token을 재발급받습니다.
- **인증**: 필요 없음 (Request Body의 Refresh Token으로 검증)

**Request Body**
| 필드 | 타입 | 제약조건 | 설명 |
|---|---|---|---|
| `refreshToken` | String | `Not Null` | 기존에 발급받은 Refresh Token |

**Success Response (`200 OK`)**
| 필드 | 타입 | 설명 |
|---|---|---|
| `accessToken` | String | 새로 발급된 JWT Access Token |

---

### 6. UI 구성 요소
- **로그인 페이지**:
  - 자체 로그인 폼 (이메일, 비밀번호 입력)
  - 회원가입, 비밀번호 찾기 링크
  - 소셜 로그인 버튼 (Google, Naver, Kakao)
  - 로그인 상태 알림 및 에러 메시지 영역
- **회원가입 페이지**:
  - 이메일, 비밀번호, 비밀번호 확인, 닉네임 입력 폼
  - 각 필드별 유효성 검사 메시지 영역  

---
---

## F-02 학습 분야 생성 및 관리

### 개요  
- 사용자가 자유롭게 학습 분야(주제)를 생성하고 관리합니다.
- 각 분야는 노트 및 문제와 연결됩니다.

### API 상세 명세

#### 1. `POST /api/v1/subjects`
- **설명**: 새로운 학습 분야를 생성합니다.
- **인증**: 필요 (Bearer Token)

**Request Body**
| 필드 | 타입 | 제약조건 | 설명 |
|---|---|---|---|
| `name` | String | `Not Null`, `Max 100` | 생성할 분야의 이름 |

**Success Response (`201 Created`)**
| 필드 | 타입 | 설명 |
|---|---|---|
| `id` | UUID | 생성된 분야의 고유 ID |
| `name` | String | 생성된 분야의 이름 |

**Error Responses**
| Status Code | errorCode | 설명 |
|---|---|---|
| `400 Bad Request` | `NAME_REQUIRED` | `name` 필드가 누락된 경우 |
| `401 Unauthorized` | `UNAUTHORIZED` | 인증되지 않은 사용자의 요청 |
| `409 Conflict` | `SUBJECT_NAME_DUPLICATED` | 이미 동일한 이름의 분야가 존재하는 경우 |

---

#### 2. `GET /api/v1/subjects`
- **설명**: 현재 사용자가 생성한 모든 학습 분야 목록을 조회합니다.
- **인증**: 필요 (Bearer Token)

**Success Response (`200 OK`)**
- **Response Body**: `Array<Object>`
| 필드 | 타입 | 설명 |
|---|---|---|
| `id` | UUID | 분야 고유 ID |
| `name` | String | 분야 이름 |

**Response 예시**
```json
[
  {
    "id": "a1b2c3d4-e5f6-7890-1234-567890abcdef",
    "name": "자료구조"
  },
  {
    "id": "b2c3d4e5-f6a7-8901-2345-67890abcdef1",
    "name": "네트워크"
  }
]
```
---

#### 3. `PUT /api/v1/subjects/{subjectId}`
- **설명**: 특정 학습 분야의 이름을 수정합니다.
- **인증**: 필요 (Bearer Token)

**Path Parameter**
| 필드 | 타입 | 설명 |
|---|---|---|
| `subjectId` | UUID | 수정할 분야의 ID |

**Request Body**
| 필드 | 타입 | 제약조건 | 설명 |
|---|---|---|---|
| `name` | String | `Not Null`, `Max 100` | 변경할 분야의 새 이름 |

**Success Response (`200 OK`)**
| 필드 | 타입 | 설명 |
|---|---|---|
| `id` | UUID | 수정된 분야의 고유 ID |
| `name` | String | 수정된 분야의 이름 |

**Error Responses**
| Status Code | errorCode | 설명 |
|---|---|---|
| `403 Forbidden` | `FORBIDDEN` | 해당 분야를 수정할 권한이 없는 경우 |
| `404 Not Found` | `SUBJECT_NOT_FOUND` | 해당 ID의 분야가 존재하지 않는 경우 |

---

#### 4. `DELETE /api/v1/subjects/{subjectId}`
- **설명**: 특정 학습 분야를 삭제합니다. 해당 분야에 속한 하위 카테고리, 노트, 문제 등도 함께 삭제됩니다.
- **인증**: 필요 (Bearer Token)

**Path Parameter**
| 필드 | 타입 | 설명 |
|---|---|---|
| `subjectId` | UUID | 삭제할 분야의 ID |

**Success Response (`204 No Content`)**
- Body 없음.

**Error Responses**
| Status Code | errorCode | 설명 |
|---|---|---|
| `403 Forbidden` | `FORBIDDEN` | 해당 분야를 삭제할 권한이 없는 경우 |
| `404 Not Found` | `SUBJECT_NOT_FOUND` | 해당 ID의 분야가 존재하지 않는 경우 |

---
---

## F-03 학습 노트 에디터

### 개요  
- 사용자가 선택한 학습 분야 안에 자유롭게 노트를 작성하고 관리합니다.
- 에디터는 TipTap 라이브러리를 사용합니다.

### API 상세 명세

#### 1. `POST /api/v1/notes`
- **설명**: 새로운 노트를 생성합니다.
- **인증**: 필요 (Bearer Token)

**Request Body**
| 필드 | 타입 | 제약조건 | 설명 |
|---|---|---|---|
| `subjectId` | UUID | `Not Null` | 노트가 소속될 학습 분야 ID |
| `categoryId`| UUID | | 노트가 소속될 카테고리 ID (선택) |
| `title` | String | `Not Null`, `Max 255` | 노트 제목 |
| `content` | String | | 노트 내용 (HTML 또는 Markdown) |

**Success Response (`201 Created`)**
| 필드 | 타입 | 설명 |
|---|---|---|
| `id` | UUID | 생성된 노트의 고유 ID |

---

#### 2. `GET /api/v1/notes`
- **설명**: 특정 조건에 맞는 노트 목록을 조회합니다. (제목, 생성일 등 기본 정보만 포함)
- **인증**: 필요 (Bearer Token)

**Query Parameters**
| 필드 | 타입 | 설명 |
|---|---|---|
| `subjectId` | UUID | 조회할 노트가 속한 학습 분야 ID (필수) |
| `categoryId`| UUID | 조회할 노트가 속한 카테고리 ID (선택) |

**Success Response (`200 OK`)**
- **Response Body**: `Array<Object>`
| 필드 | 타입 | 설명 |
|---|---|---|
| `id` | UUID | 노트 고유 ID |
| `title` | String | 노트 제목 |
| `createdAt` | Timestamp | 생성 일시 |

---

#### 3. `GET /api/v1/notes/{noteId}`
- **설명**: 특정 노트의 상세 내용을 조회합니다.
- **인증**: 필요 (Bearer Token)

**Path Parameter**
| 필드 | 타입 | 설명 |
|---|---|---|
| `noteId` | UUID | 조회할 노트의 ID |

**Success Response (`200 OK`)**
| 필드 | 타입 | 설명 |
|---|---|---|
| `id` | UUID | 노트 고유 ID |
| `title` | String | 노트 제목 |
| `content` | String | 노트 내용 |
| `subjectId` | UUID | 소속된 학습 분야 ID |
| `categoryId`| UUID | 소속된 카테고리 ID |
| `createdAt` | Timestamp | 생성 일시 |

**Error Responses**
| Status Code | errorCode | 설명 |
|---|---|---|
| `404 Not Found` | `NOTE_NOT_FOUND` | 해당 ID의 노트가 존재하지 않는 경우 |

---

#### 4. `PUT /api/v1/notes/{noteId}`
- **설명**: 특정 노트의 제목 또는 내용을 수정합니다.
- **인증**: 필요 (Bearer Token)

**Request Body**
| 필드 | 타입 | 설명 |
|---|---|---|
| `title` | String | 변경할 제목 (선택) |
| `content` | String | 변경할 내용 (선택) |

**Success Response (`200 OK`)**
- 수정된 노트의 전체 정보를 반환합니다. (GET /api/v1/notes/{noteId}와 동일)

---

#### 5. `DELETE /api/v1/notes/{noteId}`
- **설명**: 특정 노트를 삭제합니다.
- **인증**: 필요 (Bearer Token)

**Success Response (`204 No Content`)**
- Body 없음.

---
---

## F-04 문제 생성 및 풀이

### 개요
- 사용자가 학습 분야에 속한 문제를 생성하고 풀이합니다.
- 주관식/단답형 문제는 LLM을 통해 채점될 수 있습니다.

### API 상세 명세

#### 1. `POST /api/v1/problems`
- **설명**: 새로운 문제를 생성합니다.
- **인증**: 필요 (Bearer Token)

**Request Body**
| 필드 | 타입 | 제약조건 | 설명 |
|---|---|---|---|
| `subjectId` | UUID | `Not Null` | 문제가 소속될 학습 분야 ID |
| `type` | String | `Enum`("objective", "subjective") | 문제 유형 |
| `question` | String | `Not Null` | 문제 내용 (Text, JSON) |
| `answer` | String | `Not Null` | 문제 정답 |
| `explanation`| String | | 문제 해설 (선택) |
| `choices` | Array<String> | | 객관식 보기 (type이 'objective'일 경우) |

**Success Response (`201 Created`)**
| 필드 | 타입 | 설명 |
|---|---|---|
| `id` | UUID | 생성된 문제의 고유 ID |

---

#### 2. `GET /api/v1/problems`
- **설명**: 특정 학습 분야에 속한 문제 목록을 조회합니다.
- **인증**: 필요 (Bearer Token)

**Query Parameters**
| 필드 | 타입 | 설명 |
|---|---|---|
| `subjectId` | UUID | 조회할 문제가 속한 학습 분야 ID (필수) |

**Success Response (`200 OK`)**
- **Response Body**: `Array<Object>`
| 필드 | 타입 | 설명 |
|---|---|---|
| `id` | UUID | 문제 고유 ID |
| `type` | String | 문제 유형 |
| `question` | String | 문제 내용 |

---

#### 3. `POST /api/v1/problem-attempts`
- **설명**: 특정 문제에 대한 사용자의 풀이 결과를 제출하고 기록합니다.
- **인증**: 필요 (Bearer Token)

**Request Body**
| 필드 | 타입 | 설명 |
|---|---|---|
| `problemId` | UUID | 풀이한 문제의 ID |
| `userAnswer` | String | 사용자가 제출한 답안 |

**Success Response (`200 OK`)**
| 필드 | 타입 | 설명 |
|---|---|---|
| `isCorrect` | Boolean | 정답 여부 |
| `explanation` | String | 문제 해설 |
| `evaluation` | String | (주관식) LLM 채점 결과 |

---
---

## F-05 오답노트 관리

### 개요
- 틀린 문제나 북마크한 문제를 모아서 복습합니다.

### API 상세 명세

#### 1. `GET /api/v1/wrong-notes`
- **설명**: 사용자의 오답노트 목록을 조회합니다.
- **인증**: 필요 (Bearer Token)

**Query Parameters**
| 필드 | 타입 | 설명 |
|---|---|---|
| `subjectId` | UUID | 필터링할 학습 분야 ID (선택) |
| `bookmarked`| Boolean | 북마크된 문제만 볼지 여부 (선택) |

**Success Response (`200 OK`)**
- **Response Body**: `Array<Object>`
| 필드 | 타입 | 설명 |
|---|---|---|
| `problemId` | UUID | 문제 ID |
| `question` | String | 문제 내용 |
| `type` | String | 문제 유형 |
| `savedAt` | Timestamp | 오답노트에 저장된 시간 |

---

#### 2. `POST /api/v1/problems/{problemId}/bookmark`
- **설명**: 특정 문제를 북마크(오답노트에 추가)합니다.
- **인증**: 필요 (Bearer Token)

**Success Response (`201 Created`)**
- Body 없음.

---

#### 3. `DELETE /api/v1/problems/{problemId}/bookmark`
- **설명**: 특정 문제의 북마크를 해제합니다.
- **인증**: 필요 (Bearer Token)

**Success Response (`204 No Content`)**
- Body 없음.

---
---

## F-06 암기 세션 모드

### 개요
- 노트 내용을 기반으로 플래시카드 형태의 암기 학습을 진행합니다.

### API 상세 명세

#### 1. `POST /api/v1/memorization-sessions`
- **설명**: 암기 세션을 생성합니다. 특정 분야 또는 특정 노트들을 대상으로 세션을 만들 수 있습니다.
- **인증**: 필요 (Bearer Token)

**Request Body**
| 필드 | 타입 | 설명 |
|---|---|---|
| `subjectId` | UUID | 분야 전체를 대상으로 할 경우 (선택) |
| `noteIds` | Array<UUID> | 특정 노트들을 대상으로 할 경우 (선택) |

**Success Response (`201 Created`)**
| 필드 | 타입 | 설명 |
|---|---|---|
| `sessionId` | UUID | 생성된 암기 세션의 ID |
| `cards` | Array<Object> | 세션에 포함된 암기 카드 목록 |
| `cards[].noteId` | UUID | 원본 노트 ID |
| `cards[].question` | String | 카드 앞면 (노트 제목 등) |
| `cards[].answer` | String | 카드 뒷면 (노트 내용 요약 등) |

---

#### 2. `POST /api/v1/memorization-sessions/{sessionId}/attempts`
- **설명**: 암기 세션의 카드에 대한 암기 여부를 기록합니다.
- **인증**: 필요 (Bearer Token)

**Request Body**
| 필드 | 타입 | 설명 |
|---|---|---|
| `noteId` | UUID | 암기 여부를 기록할 노트(카드) ID |
| `isMemorized` | Boolean | 암기 완료 여부 |

**Success Response (`200 OK`)**
- Body 없음.

---
---

## F-07 학습 일정 캘린더 & 진행 관리

### 개요
- 날짜별 학습 목표를 기록하고 완료 여부를 관리합니다.

### API 상세 명세

#### 1. `POST /api/v1/tasks`
- **설명**: 특정 날짜에 새로운 할 일(Task)을 추가합니다.
- **인증**: 필요 (Bearer Token)

**Request Body**
| 필드 | 타입 | 제약조건 | 설명 |
|---|---|---|---|
| `dueDate` | Date | `Not Null`, `YYYY-MM-DD` | 할 일의 마감 날짜 |
| `content` | String | `Not Null` | 할 일 내용 |

**Success Response (`201 Created`)**
| 필드 | 타입 | 설명 |
|---|---|---|
| `id` | UUID | 생성된 할 일의 ID |

---

#### 2. `GET /api/v1/tasks`
- **설명**: 특정 기간의 할 일 목록을 조회합니다.
- **인증**: 필요 (Bearer Token)

**Query Parameters**
| 필드 | 타입 | 제약조건 | 설명 |
|---|---|---|---|
| `year` | Integer | `Not Null` | 조회할 년도 |
| `month` | Integer | `Not Null` | 조회할 월 |

**Success Response (`200 OK`)**
- **Response Body**: `Array<Object>`
| 필드 | 타입 | 설명 |
|---|---|---|
| `id` | UUID | 할 일 ID |
| `dueDate` | Date | 마감 날짜 |
| `content` | String | 할 일 내용 |
| `isCompleted` | Boolean | 완료 여부 |

---

#### 3. `PUT /api/v1/tasks/{taskId}`
- **설명**: 특정 할 일의 내용 또는 완료 상태를 수정합니다.
- **인증**: 필요 (Bearer Token)

**Request Body**
| 필드 | 타입 | 설명 |
|---|---|---|
| `content` | String | 수정할 내용 (선택) |
| `isCompleted` | Boolean | 수정할 완료 상태 (선택) |

**Success Response (`200 OK`)**
- 수정된 할 일의 전체 정보를 반환합니다.

---

#### 4. `DELETE /api/v1/tasks/{taskId}`
- **설명**: 특정 할 일을 삭제합니다.
- **인증**: 필요 (Bearer Token)

**Success Response (`204 No Content`)**
- Body 없음.
