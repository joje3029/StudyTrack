
# 📘 ERD 문서 - 학습 플랫폼

이 문서는 현재까지 논의된 내용을 바탕으로 구성된 ERD 구조를 정리한 것입니다.  
기능 명세서 및 개발 계획에 따라 지속적으로 확장 또는 수정될 수 있습니다.


[dbdiagram.io에서 ERD 시각화하기](https://dbdiagram.io/home)

위 사이트에 `studytrack-erd/ERD_Spec.dbml` 파일을 업로드하면 ERD를 시각적으로 확인할 수 있습니다. 
또한, export 기능을 통해 ERD 이미지를 PNG 등으로 저장할 수 있습니다.
PNG 파일명은 제작일과 버전을 포함하여 `studytrack_YYYYMMDD01.png` 형식으로 정리합니다. (예: studytrack_2025072501.png)

---

## 🧍‍♂️ User

| 필드 | 타입 | 설명 |
|------|------|------|
| id | UUID | PK |
| nickname | string | 사용자 이름 |
| status | enum | 'active', 'deleted' |
| deleted_at | datetime | 탈퇴요청일 |
| created_at | datetime | 가입일 |

---

## 📁 Subject

| 필드 | 타입 | 설명 |
|------|------|------|
| id | UUID | PK |
| name | string | 분야 이름 |

---

## 🗂 Category

| 필드 | 타입 | 설명 |
|------|------|------|
| id | UUID | PK |
| subject_id | UUID | 소속 분야 |
| name | string | 분류 이름 |

---

## 📝 Note

| 필드 | 타입 | 설명 |
|------|------|------|
| id | UUID | PK |
| user_id | UUID | 사용자 |
| subject_id | UUID | 분야 |
| category_id | UUID | 분류 |
| title | string | 제목 |
| content | text | 내용 |
| created_at | datetime | 생성일 |

---

## ❓ Problem

| 필드 | 타입 | 설명 |
|------|------|------|
| id | UUID | PK |
| subject_id | UUID | 분야 |
| category_id | UUID | 분류 |
| type | enum | 문제 유형 (객관식, 주관식 등) |
| question | text | 문제 내용 |
| answer | text | 정답 |
| explanation | text | 해설 |
| created_at | datetime | 등록일 |

---

## ✅ ProblemAttempt

| 필드 | 타입 | 설명 |
|------|------|------|
| id | UUID | PK |
| user_id | UUID | 사용자 |
| problem_id | UUID | 문제 |
| is_correct | boolean | 정답 여부 |
| attempted_at | datetime | 시도 일시 |

---

## 🔴 WrongNote

| 필드 | 타입 | 설명 |
|------|------|------|
| id | UUID | PK |
| user_id | UUID | 사용자 |
| problem_id | UUID | 문제 |
| saved_at | datetime | 저장 시점 |

---

## 🗓 Task

| 필드 | 타입 | 설명 |
|------|------|------|
| id | UUID | PK |
| user_id | UUID | 사용자 |
| content | string | 내용 |
| is_goal | boolean | 장기 목표 여부 |
| is_completed | boolean | 완료 여부 |
| due_date | date | 마감일 |
| completed_at | datetime | 완료일 |
