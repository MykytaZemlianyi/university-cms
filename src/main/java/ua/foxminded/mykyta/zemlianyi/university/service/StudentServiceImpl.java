package ua.foxminded.mykyta.zemlianyi.university.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.foxminded.mykyta.zemlianyi.university.Constants;
import ua.foxminded.mykyta.zemlianyi.university.dao.StudentDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Student;

public class StudentServiceImpl implements StudentService {
    private static Logger logger = LogManager.getLogger(StudentServiceImpl.class.getName());
    private StudentDao studentDao;

    public StudentServiceImpl(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Override
    public Student addNew(Student student) {
        verifyObject(student);
        logger.info("Adding new student - {}", student);
        return studentDao.save(student);
    }

    @Override
    public Student update(Student student) {
        verifyObject(student);
        if (studentDao.existsById(student.getId())) {
            logger.info("Updating student - {}", student);
            return studentDao.save(student);
        } else {
            throw new IllegalArgumentException(Constants.STUDENT_UPDATE_FAIL_DOES_NOT_EXIST);
        }
    }

    @Override
    public Student changePassword(Student student) {
        verifyObject(student);

        Optional<Student> managedStudentOptional = studentDao.findById(student.getId());

        if (managedStudentOptional.isPresent()) {
            Student managedStudent = managedStudentOptional.get();
            managedStudent.setPassword(student.getPassword());
            logger.info("Changed password for student - {}", student);
            return studentDao.save(managedStudent);
        } else {
            throw new IllegalArgumentException(Constants.STUDENT_PASSWORD_CHANGE_ERROR);
        }
    }

    private void verifyObject(Student student) {
        if (student == null || !student.verify()) {
            throw new IllegalArgumentException(Constants.STUDENT_INVALID);
        }
    }

}
