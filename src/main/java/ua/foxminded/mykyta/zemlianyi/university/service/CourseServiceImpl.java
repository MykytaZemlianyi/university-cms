package ua.foxminded.mykyta.zemlianyi.university.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        ObjectChecker.checkNullAndVerify(course);
        if (courseDao.existsByName(course.getName())) {
            throw new IllegalArgumentException(course.getName() + Constants.COURSE_ADD_NEW_ERROR_EXISTS_BY_NAME);
        }

        logger.info("Adding new course - {}", course.getName());
        return courseDao.save(course);
    }

    @Override
    public Course update(Course course) {
        ObjectChecker.checkNullAndVerify(course);

        ObjectChecker.checkIfExistsInDb(course, courseDao);
        logger.info("Updating course - {}", course);
        return courseDao.save(course);
    }

    @Override
    public void delete(Course course) {
        ObjectChecker.checkNullAndVerify(course);

        ObjectChecker.checkIfExistsInDb(course, courseDao);
        logger.info("Deleting course - {}", course);
        courseDao.delete(course);
    }

    @Override
    public Page<Course> findAll(Pageable pageable) {
        return courseDao.findAll(pageable);
    }

    @Override
    public List<Course> findForTeacher(Teacher teacher) {
        ObjectChecker.checkNullAndVerify(teacher);

        logger.info("Looking for courses for teacher {}", teacher);
        return courseDao.findByTeacher(teacher);
    }

    @Override
    public List<Course> findForStduent(Student student) {
        if (student == null || student.getId() == null) {
            throw new IllegalArgumentException("Student" + Constants.USER_INVALID);
        } else if (student.getGroup() == null) {
            throw new IllegalArgumentException(Constants.STUDENT_DOES_NOT_HAVE_GROUP);
        }
        logger.info("looking for courses for student {} in group {}", student, student.getGroup());
        return courseDao.findByGroups(student.getGroup());
    }

}
