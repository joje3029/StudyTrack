-- StudyTrack 데이터베이스 스키마 (Updated)
-- PostgreSQL 15+ 버전 호환

-- 1. User 테이블 (인증 정보 포함)
CREATE TABLE "user" (
  "id" uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  "email" varchar(255) UNIQUE NOT NULL,
  "password" varchar(255),                    -- 자체 로그인용 (소셜 로그인 시 NULL)
  "nickname" varchar(100) NOT NULL,
  "provider" varchar(20) NOT NULL DEFAULT 'self',  -- 'self', 'google', 'naver', 'kakao'
  "social_id" varchar(255),                   -- 소셜 로그인 고유 ID
  "status" varchar(20) NOT NULL DEFAULT 'active',  -- 'active', 'inactive', 'suspended'
  "email_verified" boolean DEFAULT false,
  "profile_image_url" varchar(500),
  "last_login_at" timestamp,
  "deleted_at" timestamp,
  "created_at" timestamp DEFAULT CURRENT_TIMESTAMP,
  "updated_at" timestamp DEFAULT CURRENT_TIMESTAMP,

  -- 제약조건
  CONSTRAINT chk_user_provider CHECK (provider IN ('self', 'google', 'naver', 'kakao')),
  CONSTRAINT chk_user_status CHECK (status IN ('active', 'inactive', 'suspended')),
  CONSTRAINT chk_user_self_login CHECK (
    (provider = 'self' AND password IS NOT NULL AND social_id IS NULL) OR
    (provider != 'self' AND password IS NULL AND social_id IS NOT NULL)
  ),
  CONSTRAINT chk_user_social_unique UNIQUE (provider, social_id)
);

-- 2. Refresh Token 테이블
CREATE TABLE "refresh_tokens" (
  "id" uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  "user_id" uuid NOT NULL,
  "token_id" varchar(255) NOT NULL UNIQUE,   -- JWT의 jti 값
  "token_hash" varchar(255) NOT NULL,        -- Refresh Token의 해시값
  "expires_at" timestamp NOT NULL,
  "created_at" timestamp DEFAULT CURRENT_TIMESTAMP,
  "last_used_at" timestamp,
  "is_revoked" boolean DEFAULT false,
  "device_info" varchar(500),                -- User-Agent, IP 등
  "ip_address" inet
);

-- 3. Subject 테이블 (사용자별 학습 분야)
CREATE TABLE "subject" (
  "id" uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  "user_id" uuid NOT NULL,
  "name" varchar(100) NOT NULL,
  "description" text,
  "color" varchar(7) DEFAULT '#3B82F6',      -- 분야별 색상 (Hex)
  "sort_order" integer DEFAULT 0,
  "created_at" timestamp DEFAULT CURRENT_TIMESTAMP,
  "updated_at" timestamp DEFAULT CURRENT_TIMESTAMP
);

-- 4. Category 테이블
CREATE TABLE "category" (
  "id" uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  "subject_id" uuid NOT NULL,
  "name" varchar(100) NOT NULL,
  "description" text,
  "sort_order" integer DEFAULT 0,
  "created_at" timestamp DEFAULT CURRENT_TIMESTAMP,
  "updated_at" timestamp DEFAULT CURRENT_TIMESTAMP
);

-- 5. Note 테이블 (향상된 기능)
CREATE TABLE "note" (
  "id" uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  "user_id" uuid NOT NULL,
  "subject_id" uuid NOT NULL,
  "category_id" uuid,                        -- NULL 허용 (미분류)
  "title" varchar(255) NOT NULL,
  "content" text NOT NULL,
  "content_type" varchar(20) DEFAULT 'html', -- 'html', 'markdown'
  "is_favorite" boolean DEFAULT false,
  "view_count" integer DEFAULT 0,
  "created_at" timestamp DEFAULT CURRENT_TIMESTAMP,
  "updated_at" timestamp DEFAULT CURRENT_TIMESTAMP
);

-- 6. Problem 테이블 (향상된 기능)
CREATE TABLE "problem" (
  "id" uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  "user_id" uuid NOT NULL,                  -- 문제 작성자
  "subject_id" uuid NOT NULL,
  "category_id" uuid,
  "type" varchar(20) NOT NULL DEFAULT 'subjective', -- 'multiple_choice', 'subjective', 'true_false'
  "question" text NOT NULL,
  "answer" text NOT NULL,
  "explanation" text,
  "options" jsonb,                          -- 객관식 선택지 (JSON 배열)
  "difficulty" integer DEFAULT 1,          -- 1(쉬움) ~ 5(어려움)
  "tags" jsonb,                            -- 태그 배열
  "is_public" boolean DEFAULT false,       -- 공개 문제 여부
  "solve_count" integer DEFAULT 0,         -- 총 풀이 횟수
  "correct_count" integer DEFAULT 0,       -- 정답 횟수
  "created_at" timestamp DEFAULT CURRENT_TIMESTAMP,
  "updated_at" timestamp DEFAULT CURRENT_TIMESTAMP
);

-- 7. Problem Attempt 테이블 (상세한 시도 기록)
CREATE TABLE "problem_attempt" (
  "id" uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  "user_id" uuid NOT NULL,
  "problem_id" uuid NOT NULL,
  "user_answer" text NOT NULL,
  "is_correct" boolean NOT NULL,
  "score" integer DEFAULT 0,               -- 0~100점
  "time_taken" integer,                    -- 소요 시간 (초)
  "llm_feedback" text,                     -- LLM 채점 피드백
  "attempt_number" integer NOT NULL,      -- 시도 횟수 (1, 2, 3, ...)
  "attempted_at" timestamp DEFAULT CURRENT_TIMESTAMP
);

-- 8. Wrong Note 테이블 (오답 관리)
CREATE TABLE "wrong_note" (
  "id" uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  "user_id" uuid NOT NULL,
  "problem_id" uuid NOT NULL,
  "attempt_id" uuid NOT NULL,              -- 틀린 시도 참조
  "status" varchar(20) DEFAULT 'active',   -- 'active', 'resolved', 'bookmarked'
  "note" text,                             -- 사용자 메모
  "resolved_at" timestamp,                 -- 해결한 시점
  "saved_at" timestamp DEFAULT CURRENT_TIMESTAMP
);

-- 9. Task 테이블 (학습 목표 관리)
CREATE TABLE "task" (
  "id" uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  "user_id" uuid NOT NULL,
  "content" varchar(500) NOT NULL,
  "is_goal" boolean DEFAULT true,          -- 목표 여부
  "is_completed" boolean DEFAULT false,
  "priority" integer DEFAULT 2,           -- 1(높음), 2(보통), 3(낮음)
  "due_date" date,
  "completed_at" timestamp,
  "created_at" timestamp DEFAULT CURRENT_TIMESTAMP,
  "updated_at" timestamp DEFAULT CURRENT_TIMESTAMP
);

-- 10. File Attachment 테이블 (파일 관리)
CREATE TABLE "file_attachment" (
  "id" uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  "user_id" uuid NOT NULL,
  "note_id" uuid,                          -- 노트 첨부 파일
  "original_name" varchar(255) NOT NULL,
  "stored_name" varchar(255) NOT NULL,     -- 저장된 파일명 (UUID 기반)
  "file_path" varchar(500) NOT NULL,       -- 실제 저장 경로
  "file_size" bigint NOT NULL,             -- 바이트 단위
  "mime_type" varchar(100) NOT NULL,
  "file_extension" varchar(10) NOT NULL,
  "is_deleted" boolean DEFAULT false,
  "uploaded_at" timestamp DEFAULT CURRENT_TIMESTAMP
);

-- ==========================================
-- 외래 키 제약조건 추가
-- ==========================================

-- Refresh Tokens
ALTER TABLE "refresh_tokens" ADD FOREIGN KEY ("user_id") REFERENCES "user"("id") ON DELETE CASCADE;

-- Subject
ALTER TABLE "subject" ADD FOREIGN KEY ("user_id") REFERENCES "user"("id") ON DELETE CASCADE;

-- Category
ALTER TABLE "category" ADD FOREIGN KEY ("subject_id") REFERENCES "subject"("id") ON DELETE CASCADE;

-- Note
ALTER TABLE "note" ADD FOREIGN KEY ("user_id") REFERENCES "user"("id") ON DELETE CASCADE;
ALTER TABLE "note" ADD FOREIGN KEY ("subject_id") REFERENCES "subject"("id") ON DELETE CASCADE;
ALTER TABLE "note" ADD FOREIGN KEY ("category_id") REFERENCES "category"("id") ON DELETE SET NULL;

-- Problem
ALTER TABLE "problem" ADD FOREIGN KEY ("user_id") REFERENCES "user"("id") ON DELETE CASCADE;
ALTER TABLE "problem" ADD FOREIGN KEY ("subject_id") REFERENCES "subject"("id") ON DELETE CASCADE;
ALTER TABLE "problem" ADD FOREIGN KEY ("category_id") REFERENCES "category"("id") ON DELETE SET NULL;

-- Problem Attempt
ALTER TABLE "problem_attempt" ADD FOREIGN KEY ("user_id") REFERENCES "user"("id") ON DELETE CASCADE;
ALTER TABLE "problem_attempt" ADD FOREIGN KEY ("problem_id") REFERENCES "problem"("id") ON DELETE CASCADE;

-- Wrong Note
ALTER TABLE "wrong_note" ADD FOREIGN KEY ("user_id") REFERENCES "user"("id") ON DELETE CASCADE;
ALTER TABLE "wrong_note" ADD FOREIGN KEY ("problem_id") REFERENCES "problem"("id") ON DELETE CASCADE;
ALTER TABLE "wrong_note" ADD FOREIGN KEY ("attempt_id") REFERENCES "problem_attempt"("id") ON DELETE CASCADE;

-- Task
ALTER TABLE "task" ADD FOREIGN KEY ("user_id") REFERENCES "user"("id") ON DELETE CASCADE;

-- File Attachment
ALTER TABLE "file_attachment" ADD FOREIGN KEY ("user_id") REFERENCES "user"("id") ON DELETE CASCADE;
ALTER TABLE "file_attachment" ADD FOREIGN KEY ("note_id") REFERENCES "note"("id") ON DELETE CASCADE;

-- ==========================================
-- 인덱스 생성
-- ==========================================

-- User 인덱스
CREATE INDEX idx_user_email ON "user"(email);
CREATE INDEX idx_user_provider_social_id ON "user"(provider, social_id);
CREATE INDEX idx_user_status ON "user"(status);
CREATE INDEX idx_user_created_at ON "user"(created_at);
CREATE INDEX idx_user_deleted_at ON "user"(deleted_at) WHERE deleted_at IS NOT NULL;

-- Refresh Tokens 인덱스
CREATE INDEX idx_refresh_tokens_user_id ON "refresh_tokens"(user_id);
CREATE INDEX idx_refresh_tokens_token_id ON "refresh_tokens"(token_id);
CREATE INDEX idx_refresh_tokens_expires_at ON "refresh_tokens"(expires_at);
CREATE INDEX idx_refresh_tokens_revoked ON "refresh_tokens"(is_revoked);

-- Subject 인덱스
CREATE INDEX idx_subject_user_id ON "subject"(user_id);
CREATE INDEX idx_subject_sort_order ON "subject"(sort_order);

-- Category 인덱스
CREATE INDEX idx_category_subject_id ON "category"(subject_id);
CREATE INDEX idx_category_sort_order ON "category"(sort_order);

-- Note 인덱스
CREATE INDEX idx_note_user_id ON "note"(user_id);
CREATE INDEX idx_note_subject_id ON "note"(subject_id);
CREATE INDEX idx_note_category_id ON "note"(category_id);
CREATE INDEX idx_note_created_at ON "note"(created_at);
CREATE INDEX idx_note_updated_at ON "note"(updated_at);
CREATE INDEX idx_note_favorite ON "note"(is_favorite) WHERE is_favorite = true;

-- Note 전문 검색 인덱스 (한국어 지원)
CREATE INDEX idx_note_title_search ON "note" USING gin(to_tsvector('korean', title));
CREATE INDEX idx_note_content_search ON "note" USING gin(to_tsvector('korean', content));

-- Problem 인덱스
CREATE INDEX idx_problem_user_id ON "problem"(user_id);
CREATE INDEX idx_problem_subject_id ON "problem"(subject_id);
CREATE INDEX idx_problem_category_id ON "problem"(category_id);
CREATE INDEX idx_problem_type ON "problem"(type);
CREATE INDEX idx_problem_difficulty ON "problem"(difficulty);
CREATE INDEX idx_problem_public ON "problem"(is_public) WHERE is_public = true;
CREATE INDEX idx_problem_tags ON "problem" USING gin(tags);
CREATE INDEX idx_problem_created_at ON "problem"(created_at);

-- Problem 전문 검색 인덱스
CREATE INDEX idx_problem_question_search ON "problem" USING gin(to_tsvector('korean', question));

-- Problem Attempt 인덱스
CREATE INDEX idx_problem_attempt_user_id ON "problem_attempt"(user_id);
CREATE INDEX idx_problem_attempt_problem_id ON "problem_attempt"(problem_id);
CREATE INDEX idx_problem_attempt_correct ON "problem_attempt"(is_correct);
CREATE INDEX idx_problem_attempt_attempted_at ON "problem_attempt"(attempted_at);
CREATE UNIQUE INDEX idx_problem_attempt_unique ON "problem_attempt"(user_id, problem_id, attempt_number);

-- Wrong Note 인덱스
CREATE INDEX idx_wrong_note_user_id ON "wrong_note"(user_id);
CREATE INDEX idx_wrong_note_problem_id ON "wrong_note"(problem_id);
CREATE INDEX idx_wrong_note_status ON "wrong_note"(status);
CREATE INDEX idx_wrong_note_saved_at ON "wrong_note"(saved_at);

-- Task 인덱스
CREATE INDEX idx_task_user_id ON "task"(user_id);
CREATE INDEX idx_task_due_date ON "task"(due_date);
CREATE INDEX idx_task_completed ON "task"(is_completed);
CREATE INDEX idx_task_priority ON "task"(priority);
CREATE INDEX idx_task_created_at ON "task"(created_at);

-- File Attachment 인덱스
CREATE INDEX idx_file_attachment_user_id ON "file_attachment"(user_id);
CREATE INDEX idx_file_attachment_note_id ON "file_attachment"(note_id);
CREATE INDEX idx_file_attachment_deleted ON "file_attachment"(is_deleted);
CREATE INDEX idx_file_attachment_uploaded_at ON "file_attachment"(uploaded_at);

-- ==========================================
-- 제약조건 추가
-- ==========================================

-- Problem 제약조건
ALTER TABLE "problem" ADD CONSTRAINT chk_problem_type CHECK (type IN ('multiple_choice', 'subjective', 'true_false'));
ALTER TABLE "problem" ADD CONSTRAINT chk_problem_difficulty CHECK (difficulty BETWEEN 1 AND 5);

-- Problem Attempt 제약조건
ALTER TABLE "problem_attempt" ADD CONSTRAINT chk_attempt_score CHECK (score BETWEEN 0 AND 100);
ALTER TABLE "problem_attempt" ADD CONSTRAINT chk_attempt_number CHECK (attempt_number > 0);

-- Wrong Note 제약조건
ALTER TABLE "wrong_note" ADD CONSTRAINT chk_wrong_note_status CHECK (status IN ('active', 'resolved', 'bookmarked'));
ALTER TABLE "wrong_note" ADD CONSTRAINT uq_wrong_note_attempt UNIQUE (user_id, attempt_id);

-- Task 제약조건
ALTER TABLE "task" ADD CONSTRAINT chk_task_priority CHECK (priority BETWEEN 1 AND 3);

-- File Attachment 제약조건
ALTER TABLE "file_attachment" ADD CONSTRAINT chk_file_size CHECK (file_size > 0 AND file_size <= 10485760); -- 10MB 제한
ALTER TABLE "file_attachment" ADD CONSTRAINT chk_file_extension CHECK (
  file_extension IN ('jpg', 'jpeg', 'png', 'gif', 'webp', 'pdf', 'doc', 'docx', 'txt', 'md')
);

-- Subject, Category 고유 제약조건
ALTER TABLE "subject" ADD CONSTRAINT uq_subject_user_name UNIQUE (user_id, name);
ALTER TABLE "category" ADD CONSTRAINT uq_category_subject_name UNIQUE (subject_id, name);

-- Note 제약조건
ALTER TABLE "note" ADD CONSTRAINT chk_note_content_type CHECK (content_type IN ('html', 'markdown'));
