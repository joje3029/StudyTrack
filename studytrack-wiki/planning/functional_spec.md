## F-01 소셜 로그인

### 프로젝트 나눔
- Core 기능 (필수/초기 기능)
    - 로그인
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


### 개요  
- Google, Naver, Kakao 소셜 로그인 지원  
- 자체 로그인 없음  
- OAuth 2.0 프로토콜 기반 인증  
- 토큰 받아서 백엔드로 전달 → JWT 발급 후 클라이언트 저장  

### 주요 흐름

1. 사용자가 로그인 화면에서 원하는 소셜 로그인 버튼 클릭  
2. OAuth 인증 팝업/리다이렉트 진행 (각 소셜별 인증)  
3. 인증 완료 후, OAuth 액세스 토큰 클라이언트 획득  
4. 액세스 토큰을 백엔드 API에 전달  
5. 백엔드는 토큰 검증 → 사용자 정보 조회 및 신규 사용자 DB 저장  
6. JWT 토큰 발급 후 클라이언트에 전달  
7. 클라이언트는 JWT 토큰 저장 (LocalStorage 혹은 Secure Cookie)  
8. 로그인 성공 화면(메인 페이지)으로 이동  

### 예외 처리

- OAuth 인증 실패 또는 취소 시 → 로그인 실패 알림 표시  
- 백엔드 토큰 검증 실패 → 재로그인 유도  
- 네트워크 오류 시 → 적절한 오류 메시지 및 재시도 안내  

### API 상세 명세

#### `POST /api/v1/auth/login`

- **설명**: 클라이언트에서 전달받은 OAuth Access Token을 검증하고, 해당 사용자를 시스템에 로그인 처리합니다. 신규 사용자일 경우 자동으로 회원가입이 진행됩니다.
- **Content-Type**: `application/json`
- **인증**: 필요 없음

---

#### **Request Body**

| 필드 | 타입 | 제약조건 | 설명 |
|---|---|---|---|
| `provider` | String | `Enum` ("google", "kakao", "naver") | OAuth 제공자 |
| `accessToken` | String | `Not Null` | OAuth 제공자로부터 발급받은 Access Token |

**Request 예시**
```json
{
  "provider": "google",
  "accessToken": "ya29.a0AfB..."
}
```

---

#### **Success Response (`200 OK`)**

- **설명**: 로그인 또는 회원가입 성공 시, 서버에서 발급한 `accessToken`과 사용자 정보를 반환합니다.

| 필드 | 타입 | 설명 |
|---|---|---|
| `accessToken` | String | 서버가 발급한 JWT (Bearer 토큰) |
| `user` | Object | 로그인한 사용자 정보 |
| `user.id` | UUID | 사용자의 고유 ID |
| `user.nickname` | String | 사용자의 닉네임 |

**Response 예시**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": "a1b2c3d4-e5f6-7890-1234-567890abcdef",
    "nickname": "개발왕"
  }
}
```

---

#### **Error Responses**

- **공통 에러 포맷**
```json
{
  "errorCode": "string",
  "message": "string"
}
```

| Status Code | errorCode | 설명 |
|---|---|---|
| `400 Bad Request` | `INVALID_PROVIDER` | `provider` 필드에 지원하지 않는 값이 들어온 경우 |
| `400 Bad Request` | `TOKEN_NOT_PROVIDED` | `accessToken`이 누락된 경우 |
| `401 Unauthorized` | `INVALID_OAUTH_TOKEN` | OAuth Access Token이 유효하지 않은 경우 |
| `500 Internal Server Error` | `INTERNAL_SERVER_ERROR` | 서버 내부 로직 처리 중 에러 발생 |


### UI 구성 요소

- 로그인 페이지  
  - 소셜 로그인 버튼 3개 (Google, Naver, Kakao)  
  - 로그인 상태 알림 및 에러 메시지 영역  

---

### 추가 고려 사항

- JWT 저장 위치에 따른 보안 정책 수립 (XSS, CSRF 방지)  
- 토큰 갱신(Refresh Token) 정책 및 만료 처리  
- 로그아웃 시 토큰 삭제 및 세션 종료  

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
