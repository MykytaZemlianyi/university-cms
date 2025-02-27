ALTER TABLE university.lectures
    ADD course_id INTEGER,
    ADD CONSTRAINT fk_course
    FOREIGN KEY (course_id) REFERENCES university.courses(course_id);