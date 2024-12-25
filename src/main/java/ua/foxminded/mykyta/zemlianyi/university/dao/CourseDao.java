package ua.foxminded.mykyta.zemlianyi.university.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.foxminded.mykyta.zemlianyi.university.dto.Course;

public interface CourseDao extends JpaRepository<Course, Long> {

}
