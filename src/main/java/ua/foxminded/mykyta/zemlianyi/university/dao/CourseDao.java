package ua.foxminded.mykyta.zemlianyi.university.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.foxminded.mykyta.zemlianyi.university.dto.Course;
import ua.foxminded.mykyta.zemlianyi.university.dto.Group;
import ua.foxminded.mykyta.zemlianyi.university.dto.Teacher;

@Repository
public interface CourseDao extends JpaRepository<Course, Long> {

    List<Course> findByTeacher(Teacher teacher);

    List<Course> findByGroups(Group group);

}
