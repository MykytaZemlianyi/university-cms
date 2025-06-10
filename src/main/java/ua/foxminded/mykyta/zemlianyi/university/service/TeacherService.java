package ua.foxminded.mykyta.zemlianyi.university.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ua.foxminded.mykyta.zemlianyi.university.dto.Teacher;

public interface TeacherService extends UserService<Teacher> {

    Page<Teacher> findAll(Pageable pageable);

}
