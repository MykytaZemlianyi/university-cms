
-- Insert administrators
INSERT INTO university.admins (name, surname, email, password) VALUES
('Mykyta', 'Zemlianyi', 'zemlianoyne@gmail.com', '$2y$10$alcxw2.Apv5li/.EDy0Ts.3mG2RDrilCbsXnxiNSYge97UvH/0q.q'),
('John', 'Smith', 'john.smith@gmail.com', '$2y$10$AiSeFo3936ky/A9.1jH56OKnhEdah9oYongk7gnMzLg4Jr7/2h93a'),
('Emma', 'Johnson', 'emma.johnson@gmail.com', '$2y$10$luaBy1QV2cVijB5AXWQN.e5e.UGzoSaKZLdBrmitirwpOVWaTXbEC');

-- Insert groups
INSERT INTO university.groups (group_name) VALUES
('A1'),
('B2');

-- Insert teachers
INSERT INTO university.teachers (name, surname, email, password) VALUES
('Michael', 'Brown', 'michael.brown@gmail.com', '$2y$10$hqapQpbiH9om3plkNozhwO0.zNdhVnTXDhR2xoKuCzyaSnxAmECWG'),
('David', 'Wilson', 'david.wilson@gmail.com', '$2y$10$a6AQgFPmLfINbdUeQUh2zOnVdp5y9Z.wsJ3zh3O4lcwoZCnaK6Aym'),
('Sarah', 'Moore', 'sarah.moore@gmail.com', '$2y$10$08.e4O4FnAonjPqHg5Q8AOw27gT4yF83p02HOVDJ3AtLvf4jA/O5C'),
('James', 'Taylor', 'james.taylor@gmail.com', '$2y$10$mlDcrl0QFLiz03i3zDlNh.aA/OLu1XzQsZ4coKFx.VDj.qi021Ipm'),
('Olivia', 'Anderson', 'olivia.anderson@gmail.com', '$2y$10$dFy56oIpOvSLdW/rrkxHnOFD5XdBAooOQ.9ZKleAit3eu3R6Woy/a');

-- Insert courses
INSERT INTO university.courses (course_name) VALUES
('Mathematics'),
('Physics'),
('Computer Science'),
('History'),
('Biology');

-- Insert students
INSERT INTO university.students (name, surname, email, password) VALUES
('William', 'Hall', 'william.hall@gmail.com', '$2y$10$ippTY2obOHRB7NNEjnol0.O5PEV7zTMnPN69Hwz47O3Kd85.ApIoC'),
('Alexander', 'Allen', 'alexander.allen@gmail.com', '$2y$10$gAuiBiXS8Tn9GWIxnJoLfOHy3xPE8/L4kQN0QU/hN08Mk/2r/.exS'),
('Ethan', 'Young', 'ethan.young@gmail.com', '$2y$10$S5Qp9P/032CeKMuFsMQYCO1bw3OD/0WYL/MDJuLdqRVcIi5DvY6Oa'),
('Daniel', 'King', 'daniel.king@gmail.com', '$2y$10$4.zaSzfAmOHkIqqGdCAEFeWxUBVdJ6UfpJ.0Mk.wv0ZRhS19vErxy'),
('Benjamin', 'Wright', 'benjamin.wright@gmail.com', '$2y$10$2rT6yldvgl1DD7BRXKHqGuUZR29SmZxTaX7tCn9qbjTDjVVLNVOgq'),
('Matthew', 'Scott', 'matthew.scott@gmail.com', '$2y$10$k6OTojbBgFHAQff6Ln/1Qu5PTbyKPa6SYm0ZTUeUhNlpdSx0Z6QbS'),
('Lucas', 'Adams', 'lucas.adams@gmail.com', '$2y$10$8.b7B964/BZlq7tw2qj7Ou7rSO3qbd/pb7UAkHZCRSh2YaCDf8ffi'),
('Henry', 'Baker', 'henry.baker@gmail.com', '$2y$10$IgSkjHF0JQc/0YEIMX6.iOR8gMaWX6mcymAhUvxChxtgqDA0cX91m'),
('Jack', 'Harris', 'jack.harris@gmail.com', '$2y$10$PJlUFw3vRGmph3j/YsX11.cR/RXJ3kx2jQnFEmF7KHUEA2enyvd0u'),
('Logan', 'Nelson', 'logan.nelson@gmail.com', '$2y$10$92Nfi1/tBCKdamP9Z04IIuJLpQE51f21JEk9KoqDKBgOBvDozX8Bm'),
('Mason', 'Carter', 'mason.carter@gmail.com', '$2y$10$GNljGfCKCefnkQW34GgjjumG05MW.4uYIe0moanPllL3amkLCW792'),
('Jacob', 'Mitchell', 'jacob.mitchell@gmail.com', '$2y$10$8Uk3l5BktYzbpN9zTU4m8.QeVn/IrJ28ZpenICqFZf8kwZZVVYgO2'),
('Noah', 'Perez', 'noah.perez@gmail.com', '$2y$10$zhkvY703qcAMUX91iPpzXO4wFRM/c/5Nj6LZw/ECtjNAxB9.bYvBO'),
('Owen', 'Roberts', 'owen.roberts@gmail.com', '$2y$10$fs1Myo/rW3TtVXUssVAvIepQZDNS2GOdbznGM7qQPn3U0qPKA97ze'),
('Elijah', 'Turner', 'elijah.turner@gmail.com', '$2y$10$a.v.mL7Mn1ON7yZB8.nsUOhrTE0Sn9RmWKGhC.sVw.VsMZAXxc2U2');

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
