package ua.foxminded.mykyta.zemlianyi.university.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ua.foxminded.mykyta.zemlianyi.university.dao.CourseDao;
import ua.foxminded.mykyta.zemlianyi.university.dao.LectureDao;
import ua.foxminded.mykyta.zemlianyi.university.dao.RoomDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Course;
import ua.foxminded.mykyta.zemlianyi.university.dto.Lecture;
import ua.foxminded.mykyta.zemlianyi.university.dto.LectureForm;
import ua.foxminded.mykyta.zemlianyi.university.dto.Room;

@Service
public class LectureServiceImpl implements LectureService {
    private static Logger logger = LogManager.getLogger(LectureServiceImpl.class.getName());
    private LectureDao lectureDao;
    private CourseDao courseDao;
    private RoomDao roomDao;

    public LectureServiceImpl(LectureDao lectureDao, CourseDao courseDao, RoomDao roomDao) {
        this.lectureDao = lectureDao;
        this.courseDao = courseDao;
        this.roomDao = roomDao;
    }

    @Override
    public Lecture addNew(Lecture lecture) {
        ObjectChecker.checkNullAndVerify(lecture);
        logger.info("Adding new lecture - {}", lecture);
        return lectureDao.save(lecture);
    }

    @Override
    public Lecture update(Lecture lecture) {
        ObjectChecker.checkNullAndVerify(lecture);
        ObjectChecker.checkIfExistsInDb(lecture, lectureDao);
        logger.info("Updating course - {}", lecture);
        return lectureDao.save(lecture);
    }

    @Override
    public void delete(Lecture lecture) {
        ObjectChecker.checkNullAndVerify(lecture);
        ObjectChecker.checkIfExistsInDb(lecture, lectureDao);

        logger.info("Updating course - {}", lecture);
        lectureDao.delete(lecture);

    }

    @Override
    public List<Lecture> findForCourse(Course course) {
        ObjectChecker.checkNullAndVerify(course);
        logger.info("Looking for lectures for course - {}", course);
        return lectureDao.findByCourse(course);
    }

    @Override
    public List<Lecture> findForCourseInTimeInterval(Course course, LocalDateTime timeStart, LocalDateTime timeEnd) {
        ObjectChecker.checkNullAndVerify(course);

        ObjectChecker.checkInterval(timeStart, timeEnd);

        logger.info("Looking for lectures for course - {} between {} and {}", course, timeStart, timeEnd);
        return lectureDao.findByCourseAndTimeStartBetween(course, timeStart, timeEnd);

    }

    @Override
    public Page<Lecture> findAll(Pageable pageable) {
        return lectureDao.findAll(pageable);
    }

    public Lecture mapFormToLecture(LectureForm form) {
        ObjectChecker.checkNull(form);
        Lecture lecture = new Lecture();
        lecture.setId(form.getId());
        lecture.setLectureType(form.getLectureType());
        lecture.setTimeStart(LocalDateTime.of(form.getDate(), form.getTimeStart()));
        lecture.setTimeEnd(LocalDateTime.of(form.getDate(), form.getTimeEnd()));

        assignCourseForLectureById(lecture, form.getCourseId());
        assignRoomForLectureById(lecture, form.getRoomId());

        return lecture;
    }

    private void assignCourseForLectureById(Lecture lecture, Long courseId) {
        Optional<Course> courseOpt = courseDao.findById(courseId);
        if (courseOpt.isPresent()) {
            Course course = courseOpt.get();
            lecture.setCourse(course);
            course.getLectures().add(lecture);
        } else {
            throw new IllegalArgumentException("Course ID: " + courseId + " assigned to lecture does not exists in DB");
        }

    }

    private void assignRoomForLectureById(Lecture lecture, Long roomId) {
        Optional<Room> roomOpt = roomDao.findById(roomId);
        if (roomOpt.isPresent()) {
            Room room = roomOpt.get();
            lecture.setRoom(room);
            room.addLecture(lecture);
        } else {
            throw new IllegalArgumentException("Room ID: " + roomId + " assigned to lecture does not exists in DB");
        }

    }

}
