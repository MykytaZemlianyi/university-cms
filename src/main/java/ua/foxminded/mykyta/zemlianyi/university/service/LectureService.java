package ua.foxminded.mykyta.zemlianyi.university.service;

import java.time.LocalDateTime;
import java.util.List;

import ua.foxminded.mykyta.zemlianyi.university.dto.Course;
import ua.foxminded.mykyta.zemlianyi.university.dto.Lecture;

public interface LectureService {
    Lecture addNew(Lecture lecture);

    Lecture update(Lecture lecture);

    List<Lecture> findForCourse(Course course);

    List<Lecture> findForCourseInTimeInterval(Course course, LocalDateTime timeStart, LocalDateTime timeEnd);
}
