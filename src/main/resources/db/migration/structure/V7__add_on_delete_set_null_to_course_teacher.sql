
ALTER TABLE university.courses
DROP CONSTRAINT IF EXISTS fk_teacher;

ALTER TABLE university.courses
ADD CONSTRAINT fk_teacher
FOREIGN KEY (teacher_id)
REFERENCES university.teachers(id)
ON DELETE SET NULL;