INSERT INTO university.teachers (name, surname, email, password) VALUES ('Marek', 'Szepski', 'mszepski@gmail.com', 'szepski99');
INSERT INTO university.teachers (name, surname, email, password) VALUES ('Pawel', 'Prysak', 'pprysak@gmail.com', '12345');

INSERT INTO university.courses (course_name, teacher_id) VALUES ('Computer Science', 1);
INSERT INTO university.courses (course_name) VALUES ('Computer Science 2');
INSERT INTO university.courses (course_name, teacher_id) VALUES ('Computer Science 3', 1);

INSERT INTO university.groups (group_name) VALUES ('AA-11');

INSERT INTO university.groups (group_name) VALUES ('BB-22');

INSERT INTO university.rooms (room_number) VALUES (100);
INSERT INTO university.rooms (room_number) VALUES (101);

INSERT INTO university.groups_courses(group_id,course_id) VALUES (1,1);

INSERT INTO university.students (name, surname, email, password, group_id) VALUES ('Mykyta', 'Zemlianyi', 'mzemlianyi@gmail.com','mz2004', 1);
INSERT INTO university.students (name, surname, email, password) VALUES ('Maksym', 'Maksymov', 'mmaksymov@gmail.com','12345');


INSERT INTO university.lectures (lecture_type, lecture_time_start, lecture_time_end) VALUES ('LECTURE', '2025-01-15 09:00:00', '2025-01-15 10:30:00');

INSERT INTO university.lectures (lecture_type, lecture_time_start, lecture_time_end, course_id) VALUES ('LECTURE', '2025-01-16 11:00:00', '2025-01-16 12:30:00', 2);

INSERT INTO university.lectures (lecture_type, lecture_time_start, lecture_time_end, course_id) VALUES ('LECTURE', '2025-01-17 14:00:00', '2025-01-17 15:30:00', 2);

INSERT INTO university.lectures (lecture_type, lecture_time_start, lecture_time_end, course_id, room_id) VALUES ('SEMINAR', '2025-01-17 14:00:00', '2025-01-17 15:30:00', 1, 1);

INSERT INTO university.lectures (lecture_type, lecture_time_start, lecture_time_end, course_id, room_id) VALUES ('SEMINAR', '2025-07-02 14:00:00', '2025-07-02 15:30:00', 3, 1);
INSERT INTO university.lectures (lecture_type, lecture_time_start, lecture_time_end, course_id, room_id) VALUES ('SEMINAR', '2025-07-03 14:00:00', '2025-07-03 15:30:00', 2, 1);
