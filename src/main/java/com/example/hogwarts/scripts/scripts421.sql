ALTER TABLE student ADD CONSTRAINT age_constraint CHECK (age > 16);

ALTER TABLE student ADD CONSTRAINT name UNIQUE (name),
ALTER name SET NOT NULL;

ALTER TABLE faculty ADD CONSTRAINT name_color_unique UNIQUE (name, color);

ALTER TABLE student ALTER age SET DEFAULT 20;