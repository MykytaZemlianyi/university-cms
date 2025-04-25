package ua.foxminded.mykyta.zemlianyi.university.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import jakarta.validation.Valid;
import ua.foxminded.mykyta.zemlianyi.university.dto.Teacher;

public interface TeacherService extends UserService<Teacher> {

    Page<Teacher> findAll(Pageable pageable);

    Teacher resolveCourseFieldById(Teacher teacher, List<Long> selectedCoursesId);

}
