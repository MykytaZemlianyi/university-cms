
-- Insert administrators
INSERT INTO university.admins (name, surname, email, password) VALUES
('John', 'Smith', 'john.smith@gmail.com', '12345'),
('Emma', 'Johnson', 'emma.johnson@gmail.com', '54321');

-- Insert groups
INSERT INTO university.groups (group_name) VALUES
('A1'),
('B2');

-- Insert teachers
INSERT INTO university.teachers (name, surname, email, password) VALUES
('Michael', 'Brown', 'michael.brown@gmail.com', '11111'),
('David', 'Wilson', 'david.wilson@gmail.com', '22222'),
('Sarah', 'Moore', 'sarah.moore@gmail.com', '33333'),
('James', 'Taylor', 'james.taylor@gmail.com', '44444'),
('Olivia', 'Anderson', 'olivia.anderson@gmail.com', '55555');

-- Insert courses
INSERT INTO university.courses (course_name) VALUES
('Mathematics'),
('Physics'),
('Computer Science'),
('History'),
('Biology');

-- Insert students
INSERT INTO university.students (name, surname, email, password) VALUES
('William', 'Hall', 'william.hall@gmail.com', '67890'),
('Alexander', 'Allen', 'alexander.allen@gmail.com', '13579'),
('Ethan', 'Young', 'ethan.young@gmail.com', '24680'),
('Daniel', 'King', 'daniel.king@gmail.com', '11223'),
('Benjamin', 'Wright', 'benjamin.wright@gmail.com', '33445'),
('Matthew', 'Scott', 'matthew.scott@gmail.com', '55667'),
('Lucas', 'Adams', 'lucas.adams@gmail.com', '77889'),
('Henry', 'Baker', 'henry.baker@gmail.com', '99000'),
('Jack', 'Harris', 'jack.harris@gmail.com', '12321'),
('Logan', 'Nelson', 'logan.nelson@gmail.com', '43234'),
('Mason', 'Carter', 'mason.carter@gmail.com', '54345'),
('Jacob', 'Mitchell', 'jacob.mitchell@gmail.com', '65456'),
('Noah', 'Perez', 'noah.perez@gmail.com', '76567'),
('Owen', 'Roberts', 'owen.roberts@gmail.com', '87678'),
('Elijah', 'Turner', 'elijah.turner@gmail.com', '98789');

-- Insert rooms
INSERT INTO university.rooms (room_number) VALUES
(101),
(102),
(103),
(104);

-- Insert lectures
INSERT INTO university.lectures (lecture_type,lecture_time_start, lecture_time_end) VALUES
('Lec', '2025-03-01 08:00:00', '2025-03-01 09:30:00'),
('Lab', '2025-03-01 10:00:00', '2025-03-01 11:30:00'),
('Sem', '2025-03-02 08:00:00', '2025-03-02 09:30:00'),
('Lec', '2025-03-02 10:00:00', '2025-03-02 11:30:00'),
('Lab', '2025-03-03 08:00:00', '2025-03-03 09:30:00'),
('Sem', '2025-03-03 10:00:00', '2025-03-03 11:30:00'),
('Lec', '2025-03-04 08:00:00', '2025-03-04 09:30:00'),
('Lab', '2025-03-04 10:00:00', '2025-03-04 11:30:00'),
('Lec', '2025-03-05 08:00:00', '2025-03-04 09:30:00'),
('Lab', '2025-03-05 10:00:00', '2025-03-04 11:30:00'),
('Lec', '2025-03-06 08:00:00', '2025-03-04 09:30:00'),
('Lab', '2025-03-06 10:00:00', '2025-03-04 11:30:00');
