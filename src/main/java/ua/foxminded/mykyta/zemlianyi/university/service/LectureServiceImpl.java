package ua.foxminded.mykyta.zemlianyi.university.service;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.foxminded.mykyta.zemlianyi.university.dao.LectureDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Course;
import ua.foxminded.mykyta.zemlianyi.university.dto.Lecture;

public class LectureServiceImpl implements LectureService {
    private static Logger logger = LogManager.getLogger(LectureServiceImpl.class.getName());
    private LectureDao lectureDao;

    public LectureServiceImpl(LectureDao lectureDao) {
        this.lectureDao = lectureDao;
    }

    @Override
    public Lecture addNew(Lecture lecture) {
        ObjectChecker.check(lecture);
        logger.info("Adding new lecture - {}", lecture);
        return lectureDao.save(lecture);
    }

    @Override
    public Lecture update(Lecture lecture) {
        ObjectChecker.check(lecture);
        ObjectChecker.checkIfExistsInDb(lecture, lectureDao);
        logger.info("Updating course - {}", lecture);
        return lectureDao.save(lecture);
    }

    @Override
    public void delete(Lecture lecture) {
        ObjectChecker.check(lecture);
        ObjectChecker.checkIfExistsInDb(lecture, lectureDao);

        logger.info("Updating course - {}", lecture);
        lectureDao.delete(lecture);

    }

    @Override
    public List<Lecture> findForCourse(Course course) {
        ObjectChecker.check(course);
        logger.info("Looking for lectures for course - {}", course);
        return lectureDao.findByCourse(course);
    }

    @Override
    public List<Lecture> findForCourseInTimeInterval(Course course, LocalDateTime timeStart, LocalDateTime timeEnd) {
        ObjectChecker.check(course);

        ObjectChecker.checkInterval(timeStart, timeEnd);

        logger.info("Looking for lectures for course - {} between {} and {}", course, timeStart, timeEnd);
        return lectureDao.findByCourseAndTimeStartBetween(course, timeStart, timeEnd);

    }

}
