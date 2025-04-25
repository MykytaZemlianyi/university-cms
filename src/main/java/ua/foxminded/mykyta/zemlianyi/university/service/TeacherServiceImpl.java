package ua.foxminded.mykyta.zemlianyi.university.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ua.foxminded.mykyta.zemlianyi.university.dao.CourseDao;
import ua.foxminded.mykyta.zemlianyi.university.dao.TeacherDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Course;
import ua.foxminded.mykyta.zemlianyi.university.dto.Teacher;

@Service
public class TeacherServiceImpl extends UserServiceImpl<Teacher> implements TeacherService {
    private static Logger logger = LogManager.getLogger(TeacherServiceImpl.class.getName());
    private CourseDao courseDao;

    public TeacherServiceImpl(TeacherDao teacherDao, CourseDao courseDao, PasswordEncoder passwordEncoder) {
        super(teacherDao, passwordEncoder);
        this.courseDao = courseDao;
    }

    @Override
    protected Teacher mergeWithExisting(Teacher user) {
        // TODO Auto-generated method stub
        return user;
    }

    @Override
    public Teacher resolveCourseFieldById(Teacher teacher, List<Long> selectedCoursesId) {
        if (selectedCoursesId != null && !selectedCoursesId.isEmpty()) {
            Set<Course> selectedCourseList = new HashSet<>(courseDao.findAllById(selectedCoursesId));
            teacher.setCourses(selectedCourseList);
        }
        return teacher;
    }

}
