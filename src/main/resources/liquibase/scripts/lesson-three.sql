-- liquibase formatted sql

-- changeset konst:1
CREATE INDEX IF NOT EXISTS student_name ON student (name);

CREATE INDEX IF NOT EXISTS faculty_name_color ON faculty (name, color)