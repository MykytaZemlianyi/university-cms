CREATE SCHEMA IF NOT EXISTS university;

CREATE TABLE university.admins (
    admin_id SERIAL PRIMARY KEY,
    admin_name VARCHAR(25) NOT NULL,
    admin_surname VARCHAR(25) NOT NULL,
    admin_email VARCHAR(320) NOT NULL,
    admin_password VARCHAR(25) NOT NULL
);

CREATE TABLE university.teachers (
    teacher_id SERIAL PRIMARY KEY,
    teacher_name VARCHAR(25) NOT NULL,
    teacher_surname VARCHAR(25) NOT NULL,
    teacher_email VARCHAR(320) NOT NULL,
    teacher_password VARCHAR(25) NOT NULL
);

CREATE TABLE university.students (
    student_id SERIAL PRIMARY KEY,
    group_id INTEGER,
    student_name VARCHAR(25) NOT NULL,
    student_surname VARCHAR(25) NOT NULL,
    student_email VARCHAR(320) NOT NULL,
    student_password VARCHAR(25) NOT NULL
);

CREATE TABLE university.groups (
    group_id SERIAL PRIMARY KEY,
    group_name VARCHAR(5)
);

CREATE TABLE university.courses (
    course_id SERIAL PRIMARY KEY,
    teacher_id INTEGER,
    course_name VARCHAR(50) NOT NULL
);

CREATE TABLE university.rooms (
    room_id SERIAL PRIMARY KEY,
    room_number INT NOT NULL
);

CREATE TABLE university.lectures(
    lecture_id SERIAL PRIMARY KEY,
    lecture_type VARCHAR(15),
    room_id INTEGER,
    lecture_time_start TIMESTAMP,
    lecture_time_end TIMESTAMP
);


ALTER TABLE university.students
    ADD CONSTRAINT fk_group
    FOREIGN KEY(group_id) REFERENCES university.groups(group_id);
    
ALTER TABLE university.courses
    ADD CONSTRAINT fk_teacher
    FOREIGN KEY(teacher_id) REFERENCES university.teachers(teacher_id);
   
ALTER TABLE university.lectures
    ADD CONSTRAINT fk_room
    FOREIGN KEY(room_id) REFERENCES university.rooms(room_id);
    
    
CREATE TABLE university.groups_courses (
    group_id INT,
    course_id INT,
    PRIMARY KEY (group_id, course_id),
    FOREIGN KEY(group_id) REFERENCES university.groups(group_id),
    FOREIGN KEY(course_id) REFERENCES university.courses(course_id)
);

CREATE TABLE university.students_lectures (
    student_id INT,
    lecture_id INT,
    PRIMARY KEY (student_id, lecture_id),
    FOREIGN KEY(student_id) REFERENCES university.students(student_id),
    FOREIGN KEY(lecture_id) REFERENCES university.lectures(lecture_id)
);

