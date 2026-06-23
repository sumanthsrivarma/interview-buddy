-- InterviewBuddy — Initial Schema
-- V1__init_schema.sql

-- =============================================================
-- QUESTION
-- =============================================================
CREATE TABLE question (
    id                  UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    title               VARCHAR(500) NOT NULL,
    body                TEXT        NOT NULL,
    answer              TEXT,
    follow_up_probes    TEXT,
    topic               VARCHAR(50) NOT NULL,
    tech_stack          VARCHAR(50) NOT NULL,
    difficulty_level    VARCHAR(20) NOT NULL,
    experience_range_min INTEGER,
    experience_range_max INTEGER,
    tags                TEXT[]      NOT NULL DEFAULT '{}',
    is_active           BOOLEAN     NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP   NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_question_topic           ON question (topic);
CREATE INDEX idx_question_tech_stack      ON question (tech_stack);
CREATE INDEX idx_question_difficulty      ON question (difficulty_level);
CREATE INDEX idx_question_is_active       ON question (is_active);
CREATE INDEX idx_question_exp_range       ON question (experience_range_min, experience_range_max);

-- =============================================================
-- MOCK SESSION
-- =============================================================
CREATE TABLE mock_session (
    id                  UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    name                VARCHAR(255) NOT NULL,
    topic               VARCHAR(50),
    tech_stack          VARCHAR(50),
    difficulty_level    VARCHAR(20),
    experience_range    INTEGER,
    question_count      INTEGER     NOT NULL,
    status              VARCHAR(20) NOT NULL DEFAULT 'IN_PROGRESS',
    started_at          TIMESTAMP   NOT NULL DEFAULT NOW(),
    completed_at        TIMESTAMP
);

CREATE INDEX idx_mock_session_status ON mock_session (status);

-- =============================================================
-- SESSION ATTEMPT
-- =============================================================
CREATE TABLE session_attempt (
    id              UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    session_id      UUID        NOT NULL REFERENCES mock_session (id) ON DELETE CASCADE,
    question_id     UUID        NOT NULL REFERENCES question (id),
    confidence      VARCHAR(20),
    personal_note   TEXT,
    attempted_at    TIMESTAMP   NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_session_attempt_session   ON session_attempt (session_id);
CREATE INDEX idx_session_attempt_question  ON session_attempt (question_id);

-- =============================================================
-- BOOKMARK
-- =============================================================
CREATE TABLE bookmark (
    id          UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    question_id UUID        NOT NULL REFERENCES question (id) ON DELETE CASCADE,
    note        VARCHAR(1000),
    created_at  TIMESTAMP   NOT NULL DEFAULT NOW()
);

CREATE UNIQUE INDEX idx_bookmark_question ON bookmark (question_id);
