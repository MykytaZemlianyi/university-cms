package ua.foxminded.mykyta.zemlianyi.university.service;

import java.util.List;

import ua.foxminded.mykyta.zemlianyi.university.dto.Course;
import ua.foxminded.mykyta.zemlianyi.university.dto.Student;
import ua.foxminded.mykyta.zemlianyi.university.dto.Teacher;

public interface CourseService {
    Course addNew(Course course);

    Course update(Course course);

    List<Course> findAll();

    List<Course> findForTeacher(Teacher teacher);

    List<Course> findForStduent(Student student);
    
    void delete(Course course);

}
