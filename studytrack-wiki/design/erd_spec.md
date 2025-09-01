
# 📘 ERD 상세 설계서 - StudyTrack

이 문서는 StudyTrack 프로젝트의 데이터베이스 구조와 테이블 간의 관계를 상세하게 정의합니다.

[dbdiagram.io에서 ERD 시각화하기](https://dbdiagram.io/home)

---

## 📜 테이블 관계 정의

- **User (1) : Note (N)**: 한 명의 사용자는 여러 노트를 작성할 수 있습니다.
- **User (1) : ProblemAttempt (N)**: 한 명의 사용자는 여러 문제 풀이 기록을 가집니다.
- **User (1) : WrongNote (N)**: 한 명의 사용자는 여러 개의 오답 노트를 가집니다.
- **User (1) : Task (N)**: 한 명의 사용자는 여러 개의 할 일을 가집니다.

- **Subject (1) : Category (N)**: 하나의 학습 분야는 여러 카테고리를 가질 수 있습니다.
- **Subject (1) : Note (N)**: 하나의 학습 분야에는 여러 노트가 포함될 수 있습니다.
- **Subject (1) : Problem (N)**: 하나의 학습 분야에는 여러 문제가 포함될 수 있습니다.

- **Category (1) : Note (N)**: 하나의 카테고리에는 여러 노트가 속할 수 있습니다.
- **Category (1) : Problem (N)**: 하나의 카테고리에는 여러 문제가 속할 수 있습니다.

- **Problem (1) : ProblemAttempt (N)**: 하나의 문제는 여러 번 풀이될 수 있습니다.
- **Problem (1) : WrongNote (N)**: 하나의 문제는 여러 오답 노트에 기록될 수 있습니다.

---

## 🧾 테이블 상세 설명

### 🧍‍♂️ User
- **역할**: 애플리케이션의 사용자 정보를 저장합니다.
- **필드**:
| 필드 | 타입 | 제약조건 | 설명 |
|------|------|----------|------|
| id | UUID | PK | 고유 식별자 |
| nickname | VARCHAR(50) | NOT NULL | 사용자 이름 |
| status | VARCHAR(20) | NOT NULL | 계정 상태 ('active', 'deleted') |
| deleted_at | TIMESTAMP | | 탈퇴 요청일 |
| created_at | TIMESTAMP | NOT NULL | 가입일 |

### 📁 Subject
- **역할**: 사용자가 생성하는 학습 주제(분야)를 관리합니다. (예: "자료구조", "네트워크")
- **필드**:
| 필드 | 타입 | 제약조건 | 설명 |
|------|------|----------|------|
| id | UUID | PK | 고유 식별자 |
| user_id | UUID | FK (User.id) | 해당 분야를 생성한 사용자 |
| name | VARCHAR(100) | NOT NULL | 분야 이름 |

### 🗂 Category
- **역할**: `Subject`를 더 작은 단위로 분류합니다. (예: "자료구조" > "트리", "그래프")
- **필드**:
| 필드 | 타입 | 제약조건 | 설명 |
|------|------|----------|------|
| id | UUID | PK | 고유 식별자 |
| subject_id | UUID | FK (Subject.id) | 소속된 학습 분야 |
| name | VARCHAR(100) | NOT NULL | 분류 이름 |

### 📝 Note
- **역할**: 사용자가 작성한 학습 노트를 저장합니다.
- **필드**:
| 필드 | 타입 | 제약조건 | 설명 |
|------|------|----------|------|
| id | UUID | PK | 고유 식별자 |
| user_id | UUID | FK (User.id) | 작성자 |
| subject_id | UUID | FK (Subject.id) | 노트가 속한 학습 분야 |
| category_id | UUID | FK (Category.id) | 노트가 속한 분류 (Optional) |
| title | VARCHAR(255) | NOT NULL | 제목 |
| content | TEXT | | 내용 (HTML 또는 Markdown) |
| created_at | TIMESTAMP | NOT NULL | 생성일 |

### ❓ Problem
- **역할**: 사용자가 생성한 문제를 저장합니다.
- **필드**:
| 필드 | 타입 | 제약조건 | 설명 |
|------|------|----------|------|
| id | UUID | PK | 고유 식별자 |
| user_id | UUID | FK (User.id) | 출제자 |
| subject_id | UUID | FK (Subject.id) | 문제가 속한 학습 분야 |
| category_id | UUID | FK (Category.id) | 문제가 속한 분류 (Optional) |
| type | VARCHAR(30) | NOT NULL | 문제 유형 ('objective', 'subjective') |
| question | TEXT | NOT NULL | 문제 내용 |
| answer | TEXT | NOT NULL | 정답 |
| explanation | TEXT | | 해설 |
| created_at | TIMESTAMP | NOT NULL | 등록일 |

### ✅ ProblemAttempt
- **역할**: 사용자의 문제 풀이 시도 이력을 기록합니다.
- **필드**:
| 필드 | 타입 | 제약조건 | 설명 |
|------|------|----------|------|
| id | UUID | PK | 고유 식별자 |
| user_id | UUID | FK (User.id) | 풀이한 사용자 |
| problem_id | UUID | FK (Problem.id) | 풀이한 문제 |
| is_correct | BOOLEAN | NOT NULL | 정답 여부 |
| attempted_at | TIMESTAMP | NOT NULL | 시도 일시 |

### 🔴 WrongNote
- **역할**: 틀린 문제 또는 사용자가 북마크한 문제를 저장하여 오답노트를 구성합니다.
- **필드**:
| 필드 | 타입 | 제약조건 | 설명 |
|------|------|----------|------|
| id | UUID | PK | 고유 식_별자 |
| user_id | UUID | FK (User.id) | 사용자 |
| problem_id | UUID | FK (Problem.id) | 오답노트에 저장된 문제 |
| saved_at | TIMESTAMP | NOT NULL | 저장 시점 |

### 🗓 Task
- **역할**: 사용자의 할 일 목록(캘린더)을 관리합니다.
- **필드**:
| 필드 | 타입 | 제약조건 | 설명 |
|------|------|----------|------|
| id | UUID | PK | 고유 식별자 |
| user_id | UUID | FK (User.id) | 사용자 |
| content | VARCHAR(500) | NOT NULL | 할 일 내용 |
| is_goal | BOOLEAN | | 장기 목표 여부 |
| is_completed | BOOLEAN | NOT NULL | 완료 여부 |
| due_date | DATE | NOT NULL | 마감일 |
| completed_at | TIMESTAMP | | 완료일 |
