package ua.foxminded.mykyta.zemlianyi.university.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.foxminded.mykyta.zemlianyi.university.Constants;
import ua.foxminded.mykyta.zemlianyi.university.dao.TeacherDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Teacher;

public class TeacherServiceImpl implements TeacherService {
    private static Logger logger = LogManager.getLogger(TeacherServiceImpl.class.getName());
    private TeacherDao teacherDao;

    public TeacherServiceImpl(TeacherDao teacherDao) {
        this.teacherDao = teacherDao;
    }

    @Override
    public Teacher addNew(Teacher teacher) {
        verifyObject(teacher);
        logger.info("Adding new teacher - {}", teacher);
        return teacherDao.save(teacher);
    }

    @Override
    public Teacher update(Teacher teacher) {
        verifyObject(teacher);
        if (teacherDao.existsById(teacher.getId())) {
            logger.info("Updating teacher - {}", teacher);
            return teacherDao.save(teacher);
        } else {
            throw new IllegalArgumentException(Constants.TEACHER_UPDATE_FAIL_DOES_NOT_EXIST);
        }
    }

    @Override
    public Teacher changePassword(Teacher teacher) {
        verifyObject(teacher);

        Optional<Teacher> managedTeacherOptional = teacherDao.findById(teacher.getId());

        if (managedTeacherOptional.isPresent()) {
            Teacher managedTeacher = managedTeacherOptional.get();
            managedTeacher.setPassword(teacher.getPassword());
            logger.info("Changed password for teacher - {}", teacher);
            return teacherDao.save(managedTeacher);
        } else {
            throw new IllegalArgumentException(Constants.TEACHER_PASSWORD_CHANGE_ERROR);
        }
    }

    private void verifyObject(Teacher teacher) {
        if (teacher == null || !teacher.verify()) {
            throw new IllegalArgumentException(Constants.TEACHER_INVALID);
        }
    }

}
