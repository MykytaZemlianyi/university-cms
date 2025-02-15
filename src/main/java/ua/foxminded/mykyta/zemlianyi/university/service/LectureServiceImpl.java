package ua.foxminded.mykyta.zemlianyi.university.service;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.foxminded.mykyta.zemlianyi.university.Constants;
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
        if (lecture == null || !lecture.verify()) {
            throw new IllegalArgumentException(Constants.LECTURE_INVALID);
        }
        logger.info("Adding new lecture - {}", lecture);
        return lectureDao.save(lecture);
    }

    @Override
    public Lecture update(Lecture lecture) {
        if (lecture == null || !lecture.verify()) {
            throw new IllegalArgumentException(Constants.LECTURE_OBJECT_INVALID_MSG);
        }
        if (lectureDao.existsById(lecture.getId())) {
            logger.info("Updating course - {}", lecture);
            return lectureDao.save(lecture);
        } else {
            throw new IllegalArgumentException(Constants.LECTURE_UPDATE_FAIL_DOES_NOT_EXIST);
        }
    }

    @Override
    public void delete(Lecture lecture) {
        if (lecture == null || !lecture.verify()) {
            throw new IllegalArgumentException(Constants.LECTURE_OBJECT_INVALID_MSG);
        }
        if (lectureDao.existsById(lecture.getId())) {
            logger.info("Updating course - {}", lecture);
            lectureDao.delete(lecture);
        } else {
            throw new IllegalArgumentException(Constants.LECTURE_DELETE_FAIL_DOES_NOT_EXIST);
        }

    }

    @Override
    public List<Lecture> findForCourse(Course course) {
        if (course == null || !course.verify()) {
            throw new IllegalArgumentException(Constants.COURSE_OBJECT_INVALID_MSG);
        }
        logger.info("Looking for lectures for course - {}", course);
        return lectureDao.findByCourse(course);
    }

    @Override
    public List<Lecture> findForCourseInTimeInterval(Course course, LocalDateTime timeStart, LocalDateTime timeEnd) {
        if (course == null || !course.verify()) {
            throw new IllegalArgumentException(Constants.COURSE_OBJECT_INVALID_MSG);
        }
        if (timeEnd.isBefore(timeStart) || timeStart.isAfter(timeEnd)) {
            throw new IllegalArgumentException(Constants.TIME_INVALID);
        }
        logger.info("Looking for lectures for course - {} between {} and {}", course, timeStart, timeEnd);
        return lectureDao.findByCourseAndTimeStartBetween(course, timeStart, timeEnd);

    }

}
