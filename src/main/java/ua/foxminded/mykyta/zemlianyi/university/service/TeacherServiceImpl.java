package ua.foxminded.mykyta.zemlianyi.university.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.foxminded.mykyta.zemlianyi.university.dao.TeacherDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Teacher;

public class TeacherServiceImpl extends UserService<Teacher> implements TeacherService {
    private static Logger logger = LogManager.getLogger(TeacherServiceImpl.class.getName());

    public TeacherServiceImpl(TeacherDao teacherDao) {
        super(teacherDao);
    }

}
