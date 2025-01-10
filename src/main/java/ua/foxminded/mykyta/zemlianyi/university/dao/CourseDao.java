package ua.foxminded.mykyta.zemlianyi.university.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ua.foxminded.mykyta.zemlianyi.university.Constants;
import ua.foxminded.mykyta.zemlianyi.university.dto.Course;
import ua.foxminded.mykyta.zemlianyi.university.dto.Teacher;

@Repository
public interface CourseDao extends JpaRepository<Course, Long> {
    @Query(Constants.FIND_COURSES_BY_TEACHER)
    List<Course> findCoursesByTeacher(@Param("teacherId") Teacher teacher);

    List<Course> findByTeacher(Teacher teacher);
}
