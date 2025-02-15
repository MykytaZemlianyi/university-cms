package ua.foxminded.mykyta.zemlianyi.university.service;

import ua.foxminded.mykyta.zemlianyi.university.dto.Teacher;

public interface TeacherService {
    Teacher addNew(Teacher teacher);

    Teacher update(Teacher teacher);

    Teacher changePassword(Teacher teacher);

    void delete(Teacher teacher);
}
