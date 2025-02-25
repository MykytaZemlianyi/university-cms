INSERT INTO university.teachers (name, surname, email, password) VALUES ('Marek', 'Szepski', 'mszepski@gmail.com', 'szepski99');

INSERT INTO university.courses (course_name, teacher_id) VALUES ('Computer Science', 1);
INSERT INTO university.courses (course_name, teacher_id) VALUES ('Computer Science 2', 1);

INSERT INTO university.groups (group_name) VALUES ('AA-11');

INSERT INTO university.students (name, surname, email, password, group_id) VALUES ('Mykyta', 'Zemlianyi', 'mzemlianyi@gmail.com','mz2004', 1);

INSERT INTO university.lectures (lecture_type, course_id) VALUES ('LECTURE', 1);

INSERT INTO university.lectures (lecture_type, lecture_time_start, lecture_time_end, course_id) VALUES ('LECTURE', '2025-01-15 09:00:00', '2025-01-15 10:30:00', 2);

INSERT INTO university.lectures (lecture_type, lecture_time_start, lecture_time_end, course_id) VALUES ('LECTURE', '2025-01-16 11:00:00', '2025-01-16 12:30:00', 2);

INSERT INTO university.lectures (lecture_type, lecture_time_start, lecture_time_end, course_id) VALUES ('LECTURE', '2025-01-17 14:00:00', '2025-01-17 15:30:00', 2);
