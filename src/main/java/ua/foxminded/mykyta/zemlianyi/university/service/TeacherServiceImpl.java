package ua.foxminded.mykyta.zemlianyi.university.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.foxminded.mykyta.zemlianyi.university.Constants;
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
    protected Teacher mergeWithExisting(Teacher newTeacher) {
        ObjectChecker.checkNull(newTeacher);
        Optional<Teacher> existingTeacherOpt = dao.findById(newTeacher.getId());

        if (existingTeacherOpt.isPresent()) {
            Teacher existingTeacher = existingTeacherOpt.get();
            Teacher mergedTeacher = new Teacher();

            mergedTeacher.setId(existingTeacher.getId());
            mergedTeacher.setName(chooseString(newTeacher.getName(), existingTeacher.getName()));
            mergedTeacher.setSurname(chooseString(newTeacher.getSurname(), existingTeacher.getSurname()));
            mergedTeacher.setEmail(chooseString(newTeacher.getEmail(), existingTeacher.getEmail()));
            mergedTeacher.setCourses(newTeacher.getCourses());
            mergedTeacher.setPassword(choosePassword(newTeacher.getPassword(), existingTeacher.getPassword()));

            return mergedTeacher;

        } else {
            throw new IllegalArgumentException(Constants.USER_NOT_FOUND_ERROR);
        }
    }

    @Override
    public Teacher resolveCourseFieldById(Teacher teacher, List<Long> selectedCoursesId) {
        Set<Course> selectedCourseList = new HashSet<>(courseDao.findAllById(selectedCoursesId));
        teacher.setCourses(selectedCourseList);
        return teacher;
    }

    private String chooseString(String newString, String existingString) {
        if (newString == null || newString.isBlank() || newString.equals(existingString)) {
            return existingString;
        } else {
            return newString;
        }
    }

    private String choosePassword(String newPassword, String existingPassword) {
        if (newPassword == null || newPassword.isBlank() || newPassword.equals(existingPassword)) {
            return existingPassword;
        } else {
            return passwordEncoder.encode(newPassword);
        }
    }

}
