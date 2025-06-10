package ua.foxminded.mykyta.zemlianyi.university.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ua.foxminded.mykyta.zemlianyi.university.dto.Group;
import ua.foxminded.mykyta.zemlianyi.university.dto.Student;

public interface GroupService {
    Group addNew(Group group);

    Group update(Group group);

    Group findForStudent(Student student);

    void delete(Group group);

    Page<Group> findAll(Pageable pageable);

    List<Group> findAll();

    Optional<Group> findById(Long groupId);

    Group getByIdOrThrow(Long id);

    void deleteOrThrow(Long id);
}
