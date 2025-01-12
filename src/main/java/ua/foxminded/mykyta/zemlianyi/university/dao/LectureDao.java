package ua.foxminded.mykyta.zemlianyi.university.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.foxminded.mykyta.zemlianyi.university.dto.Course;
import ua.foxminded.mykyta.zemlianyi.university.dto.Lecture;

@Repository
public interface LectureDao extends JpaRepository<Lecture, Long> {
    List<Lecture> findByCourse(Course course);
    List<Lecture> findByCourseAndTimeStartBetween(Course course, LocalDateTime timeStart, LocalDateTime timeEnd);
}
