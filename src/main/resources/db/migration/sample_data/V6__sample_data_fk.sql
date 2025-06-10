-- Assign teachers to courses
UPDATE university.courses SET teacher_id = 1 WHERE course_name = 'Mathematics';
UPDATE university.courses SET teacher_id = 2 WHERE course_name = 'Physics';
UPDATE university.courses SET teacher_id = 3 WHERE course_name = 'Computer Science';
UPDATE university.courses SET teacher_id = 4 WHERE course_name = 'History';
UPDATE university.courses SET teacher_id = 5 WHERE course_name = 'Biology';

-- Assign students to groups
UPDATE university.students SET group_id = 1 WHERE name IN ('William', 'Alexander', 'Ethan', 'Daniel', 'Benjamin', 'Matthew', 'Lucas', 'Henry');
UPDATE university.students SET group_id = 2 WHERE name IN ('Jack', 'Logan', 'Mason', 'Jacob', 'Noah', 'Owen', 'Elijah');

-- Assign courses to lectures
UPDATE university.lectures SET course_id = 1, room_id = 1 WHERE lecture_id = 1;
UPDATE university.lectures SET course_id = 2, room_id = 2 WHERE lecture_id = 2;
UPDATE university.lectures SET course_id = 3, room_id = 3 WHERE lecture_id = 3;
UPDATE university.lectures SET course_id = 4, room_id = 4 WHERE lecture_id = 4;
UPDATE university.lectures SET course_id = 5, room_id = 1 WHERE lecture_id = 5;
UPDATE university.lectures SET course_id = 1, room_id = 2 WHERE lecture_id = 6;
UPDATE university.lectures SET course_id = 2, room_id = 3 WHERE lecture_id = 7;
UPDATE university.lectures SET course_id = 3, room_id = 4 WHERE lecture_id = 8;
UPDATE university.lectures SET course_id = 4, room_id = 1 WHERE lecture_id = 9;
UPDATE university.lectures SET course_id = 5, room_id = 2 WHERE lecture_id = 10;
UPDATE university.lectures SET course_id = 1, room_id = 3 WHERE lecture_id = 11;
UPDATE university.lectures SET course_id = 2, room_id = 4 WHERE lecture_id = 12;

-- Assign groups to courses
INSERT INTO university.groups_courses (group_id, course_id) VALUES
(1, 1), (1, 2), (1, 3),
(2, 3), (2, 4), (2, 5);
