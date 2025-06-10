package ua.foxminded.mykyta.zemlianyi.university.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ua.foxminded.mykyta.zemlianyi.university.dto.Student;

public interface StudentService extends UserService<Student> {

    Page<Student> findAll(Pageable pageable);

    Student getByIdOrThrow(Long id);

}
