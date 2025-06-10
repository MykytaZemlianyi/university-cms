ALTER TABLE university.students RENAME COLUMN student_name TO name;
ALTER TABLE university.students RENAME COLUMN student_surname TO surname;
ALTER TABLE university.students RENAME COLUMN student_email TO email;
ALTER TABLE university.students RENAME COLUMN student_password TO password;

ALTER TABLE university.teachers RENAME COLUMN teacher_name TO name;
ALTER TABLE university.teachers RENAME COLUMN teacher_surname TO surname;
ALTER TABLE university.teachers RENAME COLUMN teacher_email TO email;
ALTER TABLE university.teachers RENAME COLUMN teacher_password TO password;

ALTER TABLE university.admins RENAME COLUMN admin_name TO name;
ALTER TABLE university.admins RENAME COLUMN admin_surname TO surname;
ALTER TABLE university.admins RENAME COLUMN admin_email TO email;
ALTER TABLE university.admins RENAME COLUMN admin_password TO password;
