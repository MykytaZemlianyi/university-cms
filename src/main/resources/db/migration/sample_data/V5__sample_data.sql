
-- Insert administrators
INSERT INTO university.admins (name, surname, email, password) VALUES
('Mykyta', 'Zemlianyi', 'zemlianoyne@gmail.com', '$2a$10$S7kE9HXuqvvXsGkG2dElWeHbaJqz9ok2e3vhcfbTEtkAzI5A0V6qG'),
('John', 'Smith', 'john.smith@gmail.com', '$2a$10$S7kE9HXuqvvXsGkG2dElWeHbaJqz9ok2e3vhcfbTEtkAzI5A0V6qG'),
('Emma', 'Johnson', 'emma.johnson@gmail.com', '$2a$10$S7kE9HXuqvvXsGkG2dElWeHbaJqz9ok2e3vhcfbTEtkAzI5A0V6qG');

-- Insert groups
INSERT INTO university.groups (group_name) VALUES
('A1'),
('B2');

-- Insert teachers
INSERT INTO university.teachers (name, surname, email, password) VALUES
('Michael', 'Brown', 'michael.brown@gmail.com', '$2a$10$S7kE9HXuqvvXsGkG2dElWeHbaJqz9ok2e3vhcfbTEtkAzI5A0V6qG'),
('David', 'Wilson', 'david.wilson@gmail.com', '$2a$10$S7kE9HXuqvvXsGkG2dElWeHbaJqz9ok2e3vhcfbTEtkAzI5A0V6qG'),
('Sarah', 'Moore', 'sarah.moore@gmail.com', '$2a$10$S7kE9HXuqvvXsGkG2dElWeHbaJqz9ok2e3vhcfbTEtkAzI5A0V6qG'),
('James', 'Taylor', 'james.taylor@gmail.com', '$2a$10$S7kE9HXuqvvXsGkG2dElWeHbaJqz9ok2e3vhcfbTEtkAzI5A0V6qG'),
('Olivia', 'Anderson', 'olivia.anderson@gmail.com', '$2a$10$S7kE9HXuqvvXsGkG2dElWeHbaJqz9ok2e3vhcfbTEtkAzI5A0V6qG');

-- Insert courses
INSERT INTO university.courses (course_name) VALUES
('Mathematics'),
('Physics'),
('Computer Science'),
('History'),
('Biology');

-- Insert students
INSERT INTO university.students (name, surname, email, password) VALUES
('William', 'Hall', 'william.hall@gmail.com', '$2a$10$S7kE9HXuqvvXsGkG2dElWeHbaJqz9ok2e3vhcfbTEtkAzI5A0V6qG'),
('Alexander', 'Allen', 'alexander.allen@gmail.com', '$2a$10$S7kE9HXuqvvXsGkG2dElWeHbaJqz9ok2e3vhcfbTEtkAzI5A0V6qG'),
('Ethan', 'Young', 'ethan.young@gmail.com', '$2a$10$S7kE9HXuqvvXsGkG2dElWeHbaJqz9ok2e3vhcfbTEtkAzI5A0V6qG'),
('Daniel', 'King', 'daniel.king@gmail.com', '$2a$10$S7kE9HXuqvvXsGkG2dElWeHbaJqz9ok2e3vhcfbTEtkAzI5A0V6qG'),
('Benjamin', 'Wright', 'benjamin.wright@gmail.com', '$2a$10$S7kE9HXuqvvXsGkG2dElWeHbaJqz9ok2e3vhcfbTEtkAzI5A0V6qG'),
('Matthew', 'Scott', 'matthew.scott@gmail.com', '$2a$10$S7kE9HXuqvvXsGkG2dElWeHbaJqz9ok2e3vhcfbTEtkAzI5A0V6qG'),
('Lucas', 'Adams', 'lucas.adams@gmail.com', '$2a$10$S7kE9HXuqvvXsGkG2dElWeHbaJqz9ok2e3vhcfbTEtkAzI5A0V6qG'),
('Henry', 'Baker', 'henry.baker@gmail.com', '$2a$10$S7kE9HXuqvvXsGkG2dElWeHbaJqz9ok2e3vhcfbTEtkAzI5A0V6qG'),
('Jack', 'Harris', 'jack.harris@gmail.com', '$2a$10$S7kE9HXuqvvXsGkG2dElWeHbaJqz9ok2e3vhcfbTEtkAzI5A0V6qG'),
('Logan', 'Nelson', 'logan.nelson@gmail.com', '$2a$10$S7kE9HXuqvvXsGkG2dElWeHbaJqz9ok2e3vhcfbTEtkAzI5A0V6qG'),
('Mason', 'Carter', 'mason.carter@gmail.com', '$2a$10$S7kE9HXuqvvXsGkG2dElWeHbaJqz9ok2e3vhcfbTEtkAzI5A0V6qG'),
('Jacob', 'Mitchell', 'jacob.mitchell@gmail.com', '$2a$10$S7kE9HXuqvvXsGkG2dElWeHbaJqz9ok2e3vhcfbTEtkAzI5A0V6qG'),
('Noah', 'Perez', 'noah.perez@gmail.com', '$2a$10$S7kE9HXuqvvXsGkG2dElWeHbaJqz9ok2e3vhcfbTEtkAzI5A0V6qG'),
('Owen', 'Roberts', 'owen.roberts@gmail.com', '$2a$10$S7kE9HXuqvvXsGkG2dElWeHbaJqz9ok2e3vhcfbTEtkAzI5A0V6qG'),
('Elijah', 'Turner', 'elijah.turner@gmail.com', '$2a$10$S7kE9HXuqvvXsGkG2dElWeHbaJqz9ok2e3vhcfbTEtkAzI5A0V6qG');

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
