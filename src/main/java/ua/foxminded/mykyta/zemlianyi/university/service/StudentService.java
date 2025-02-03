package ua.foxminded.mykyta.zemlianyi.university.service;

import ua.foxminded.mykyta.zemlianyi.university.dto.Student;

public interface StudentService {
    Student addNew(Student student);

    Student update(Student student);

    Student changePassword(Student student);

}
