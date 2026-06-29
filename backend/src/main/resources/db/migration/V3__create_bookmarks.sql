CREATE TABLE bookmarks (
    id          UUID        NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    question_id UUID        NOT NULL REFERENCES question(id),
    note        TEXT,
    created_at  TIMESTAMP   NOT NULL DEFAULT now()
);
