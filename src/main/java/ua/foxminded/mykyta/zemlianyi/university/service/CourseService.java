package ua.foxminded.mykyta.zemlianyi.university.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ua.foxminded.mykyta.zemlianyi.university.dto.Course;
import ua.foxminded.mykyta.zemlianyi.university.dto.Student;
import ua.foxminded.mykyta.zemlianyi.university.dto.Teacher;

public interface CourseService {
    Course addNew(Course course);

    Course update(Course course);

    List<Course> findAll();

    Page<Course> findAll(Pageable pageable);

    List<Course> findForTeacher(Teacher teacher);

    List<Course> findForStduent(Student student);

    void delete(Course course);

    void deleteOrThrow(Long id);

    Optional<Course> findById(Long id);

    Course getByIdOrThrow(Long id);

    public List<Course> findForUserWithUsername(String username, String role);

}
