ALTER TABLE university.students
DROP CONSTRAINT IF EXISTS fk_group;

ALTER TABLE university.students
ADD CONSTRAINT fk_group
FOREIGN KEY (group_id)
REFERENCES university.groups(group_id)
ON DELETE SET NULL;