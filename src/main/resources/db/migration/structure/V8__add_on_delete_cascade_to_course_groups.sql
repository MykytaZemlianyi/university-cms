
ALTER TABLE university.groups_courses
DROP CONSTRAINT IF EXISTS groups_courses_group_id_fkey;

ALTER TABLE university.groups_courses
DROP CONSTRAINT IF EXISTS groups_courses_course_id_fkey;

ALTER TABLE university.groups_courses
ADD CONSTRAINT fk_group_id
FOREIGN KEY (group_id)
REFERENCES university.groups(group_id)
ON DELETE CASCADE;

ALTER TABLE university.groups_courses
ADD CONSTRAINT fk_course_id
FOREIGN KEY (course_id)
REFERENCES university.courses(course_id)
ON DELETE CASCADE;
