package ua.foxminded.mykyta.zemlianyi.university.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.foxminded.mykyta.zemlianyi.university.dao.StudentDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Student;

public class StudentServiceImpl extends UserServiceImpl<Student> implements StudentService {
    private static Logger logger = LogManager.getLogger(StudentServiceImpl.class.getName());

    public StudentServiceImpl(StudentDao studentDao) {
        super(studentDao);
    }

}
