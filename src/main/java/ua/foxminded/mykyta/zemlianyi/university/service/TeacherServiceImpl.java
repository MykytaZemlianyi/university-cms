package ua.foxminded.mykyta.zemlianyi.university.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ua.foxminded.mykyta.zemlianyi.university.dao.TeacherDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Teacher;

@Service
public class TeacherServiceImpl extends UserServiceImpl<Teacher> implements TeacherService {
    private static Logger logger = LogManager.getLogger(TeacherServiceImpl.class.getName());

    public TeacherServiceImpl(TeacherDao teacherDao, PasswordEncoder passwordEncoder) {
        super(teacherDao, passwordEncoder);
    }

    @Override
    protected Teacher mergeWithExisting(Teacher user) {
        // TODO Auto-generated method stub
        return user;
    }

}
