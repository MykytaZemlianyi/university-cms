package ua.foxminded.mykyta.zemlianyi.university.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import ua.foxminded.mykyta.zemlianyi.university.Constants;
import ua.foxminded.mykyta.zemlianyi.university.dao.CourseDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Course;
import ua.foxminded.mykyta.zemlianyi.university.dto.Student;
import ua.foxminded.mykyta.zemlianyi.university.dto.Teacher;

@Service
public class CourseServiceImpl implements CourseService {
    private static Logger logger = LogManager.getLogger(CourseServiceImpl.class.getName());

    private CourseDao courseDao;

    public CourseServiceImpl(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    @Override
    public Course addNew(Course course) {
        if (course == null || !course.verify()) {
            throw new IllegalArgumentException(Constants.COURSE_OBJECT_INVALID_MSG);
        }
        logger.info("Adding new course - {}", course.getName());
        return courseDao.save(course);
    }

    @Override
    public Course update(Course course) {
        if (course == null || !course.verify()) {
            throw new IllegalArgumentException(Constants.COURSE_OBJECT_INVALID_MSG);
        }
        if (courseDao.existsById(course.getId())) {
            logger.info("Updating course - {}", course);
            return courseDao.save(course);
        } else {
            throw new IllegalArgumentException(Constants.FAIL_UPDATE_COURSE_DOES_NOT_EXIST);
        }
    }

    @Override
    public List<Course> findAll() {
        return courseDao.findAll();
    }

    @Override
    public List<Course> findForTeacher(Teacher teacher) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Course> findForStduent(Student student) {
        // TODO Auto-generated method stub
        return null;
    }
}
