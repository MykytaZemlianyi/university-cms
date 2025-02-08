package ua.foxminded.mykyta.zemlianyi.university.service;

import ua.foxminded.mykyta.zemlianyi.university.dto.Group;
import ua.foxminded.mykyta.zemlianyi.university.dto.Student;

public interface GroupService {
    Group addNew(Group group);

    Group update(Group group);

    Group findForStudent(Student student);
}
