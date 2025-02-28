
-- Insert administrators
INSERT INTO university.admins (name, surname, email, password) VALUES
('Mykyta', 'Zemlianyi', 'zemlianoyne@gmail.com', '12345'),
('John', 'Smith', 'john.smith@gmail.com', '12345'),
('Emma', 'Johnson', 'emma.johnson@gmail.com', '12345');

-- Insert groups
INSERT INTO university.groups (group_name) VALUES
('A1'),
('B2');

-- Insert teachers
INSERT INTO university.teachers (name, surname, email, password) VALUES
('Michael', 'Brown', 'michael.brown@gmail.com', '12345'),
('David', 'Wilson', 'david.wilson@gmail.com', '12345'),
('Sarah', 'Moore', 'sarah.moore@gmail.com', '12345'),
('James', 'Taylor', 'james.taylor@gmail.com', '12345'),
('Olivia', 'Anderson', 'olivia.anderson@gmail.com', '12345');

-- Insert courses
INSERT INTO university.courses (course_name) VALUES
('Mathematics'),
('Physics'),
('Computer Science'),
('History'),
('Biology');

-- Insert students
INSERT INTO university.students (name, surname, email, password) VALUES
('William', 'Hall', 'william.hall@gmail.com', '12345'),
('Alexander', 'Allen', 'alexander.allen@gmail.com', '12345'),
('Ethan', 'Young', 'ethan.young@gmail.com', '12345'),
('Daniel', 'King', 'daniel.king@gmail.com', '12345'),
('Benjamin', 'Wright', 'benjamin.wright@gmail.com', '12345'),
('Matthew', 'Scott', 'matthew.scott@gmail.com', '12345'),
('Lucas', 'Adams', 'lucas.adams@gmail.com', '12345'),
('Henry', 'Baker', 'henry.baker@gmail.com', '12345'),
('Jack', 'Harris', 'jack.harris@gmail.com', '12345'),
('Logan', 'Nelson', 'logan.nelson@gmail.com', '12345'),
('Mason', 'Carter', 'mason.carter@gmail.com', '12345'),
('Jacob', 'Mitchell', 'jacob.mitchell@gmail.com', '12345'),
('Noah', 'Perez', 'noah.perez@gmail.com', '12345'),
('Owen', 'Roberts', 'owen.roberts@gmail.com', '12345'),
('Elijah', 'Turner', 'elijah.turner@gmail.com', '12345');

-- Insert rooms
INSERT INTO university.rooms (room_number) VALUES
(101),
(102),
(103),
(104);

-- Insert lectures
INSERT INTO university.lectures (lecture_type,lecture_time_start, lecture_time_end) VALUES
('LECTURE', '2025-03-01 08:00:00', '2025-03-01 09:30:00'),
('LABORATORIUM', '2025-03-01 10:00:00', '2025-03-01 11:30:00'),
('SEMINAR', '2025-03-02 08:00:00', '2025-03-02 09:30:00'),
('LECTURE', '2025-03-02 10:00:00', '2025-03-02 11:30:00'),
('LABORATORIUM', '2025-03-03 08:00:00', '2025-03-03 09:30:00'),
('SEMINAR', '2025-03-03 10:00:00', '2025-03-03 11:30:00'),
('LECTURE', '2025-03-04 08:00:00', '2025-03-04 09:30:00'),
('LABORATORIUM', '2025-03-04 10:00:00', '2025-03-04 11:30:00'),
('LECTURE', '2025-03-05 08:00:00', '2025-03-04 09:30:00'),
('SEMINAR', '2025-03-05 10:00:00', '2025-03-04 11:30:00'),
('LECTURE', '2025-03-06 08:00:00', '2025-03-04 09:30:00'),
('LABORATORIUM', '2025-03-06 10:00:00', '2025-03-04 11:30:00');
