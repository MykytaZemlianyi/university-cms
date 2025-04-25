package ua.foxminded.mykyta.zemlianyi.university.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ua.foxminded.mykyta.zemlianyi.university.dao.GroupDao;
import ua.foxminded.mykyta.zemlianyi.university.dao.StudentDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Group;
import ua.foxminded.mykyta.zemlianyi.university.dto.Student;

@Service
public class StudentServiceImpl extends UserServiceImpl<Student> implements StudentService {
    private static Logger logger = LogManager.getLogger(StudentServiceImpl.class.getName());
    private GroupDao groupDao;

    public StudentServiceImpl(StudentDao studentDao, GroupDao groupDao, PasswordEncoder passwordEncoder) {
        super(studentDao, passwordEncoder);
        this.groupDao = groupDao;
    }

    @Override
    protected Student mergeWithExisting(Student user) {
        // TODO Auto-generated method stub
        return user;
    }

    @Override
    public Student resolveGroupFieldById(Student student, Long groupId) {
        if (groupId != null) {
            Optional<Group> groupOpt = groupDao.findById(groupId);
            if (groupOpt.isPresent()) {
                student.setGroup(groupOpt.get());
            }
        }
        return student;
    }

}
