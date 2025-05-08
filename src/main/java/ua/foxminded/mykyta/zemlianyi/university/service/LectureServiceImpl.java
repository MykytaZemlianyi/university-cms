package ua.foxminded.mykyta.zemlianyi.university.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ua.foxminded.mykyta.zemlianyi.university.Constants;
import ua.foxminded.mykyta.zemlianyi.university.dao.CourseDao;
import ua.foxminded.mykyta.zemlianyi.university.dao.LectureDao;
import ua.foxminded.mykyta.zemlianyi.university.dao.RoomDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Course;
import ua.foxminded.mykyta.zemlianyi.university.dto.Lecture;
import ua.foxminded.mykyta.zemlianyi.university.dto.LectureForm;

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
        Lecture mergedLecture = mergeWithExisting(lecture);
        logger.info("Updating course - {}", mergedLecture);
        return lectureDao.save(mergedLecture);
    }

    private Lecture mergeWithExisting(Lecture lecture) {
        Optional<Lecture> existingLectureOpt = lectureDao.findById(lecture.getId());
        if (existingLectureOpt.isPresent()) {
            Lecture existingLecture = existingLectureOpt.get();
            existingLecture.setLectureType(lecture.getLectureType());
            existingLecture.setTimeStart(lecture.getTimeStart());
            existingLecture.setTimeEnd(lecture.getTimeEnd());

            mergeCourse(existingLecture, lecture);

            mergeRoom(existingLecture, lecture);

            return existingLecture;
        } else {
            throw new IllegalArgumentException(Constants.OBJECT_UPDATE_FAIL_DOES_NOT_EXIST);
        }
    }

    private void mergeCourse(Lecture existingLecture, Lecture updatedLecture) {
        if (!existingLecture.getCourse().equals(updatedLecture.getCourse())) {
            if (existingLecture.getCourse() != null) {
                existingLecture.getCourse().removeLecture(existingLecture);
            }
            existingLecture.setCourse(updatedLecture.getCourse());
            if (existingLecture.getCourse() != null) {
                existingLecture.getCourse().addLecture(existingLecture);
            }
        }
    }

    private void mergeRoom(Lecture existingLecture, Lecture updatedLecture) {
        if (existingLecture.getRoom() != null) {
            existingLecture.getRoom().removeLecture(existingLecture);
        }
        existingLecture.setRoom(updatedLecture.getRoom());
        if (existingLecture.getRoom() != null) {
            existingLecture.getRoom().addLecture(existingLecture);
        }

    }

    @Override
    public void delete(Lecture lecture) {
        ObjectChecker.checkNullAndVerify(lecture);
        ObjectChecker.checkIfExistsInDb(lecture, lectureDao);
        lecture.clearRelations();
        logger.info("Updating course - {}", lecture);
        lectureDao.delete(lecture);

    }

    @Override
    public LectureForm mapLectureToForm(Lecture lecture) {
        ObjectChecker.checkNull(lecture);
        LectureForm form = new LectureForm();
        form.setId(lecture.getId());
        form.setLectureType(lecture.getLectureType());

        form.setCourseId(lecture.getCourse() != null ? lecture.getCourse().getId() : null);
        form.setRoomId(lecture.getRoom() != null ? lecture.getRoom().getId() : null);

        form.setDate(lecture.getTimeStart().toLocalDate());
        form.setTimeStart(lecture.getTimeStart().toLocalTime());
        form.setTimeEnd(lecture.getTimeEnd().toLocalTime());

        return form;
    }

    public Lecture mapFormToLecture(LectureForm form) {
        Lecture lecture = new Lecture();
        lecture.setId(form.getId());
        lecture.setLectureType(form.getLectureType());
        lecture.setTimeStart(LocalDateTime.of(form.getDate(), form.getTimeStart()));
        lecture.setTimeEnd(LocalDateTime.of(form.getDate(), form.getTimeEnd()));

        courseDao.findById(form.getCourseId()).ifPresent(lecture::setCourse);
        roomDao.findById(form.getRoomId()).ifPresent(lecture::setRoom);

        return lecture;
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

    @Override
    public Optional<Lecture> findById(Long id) {
        return lectureDao.findById(id);
    }

}
