package ua.foxminded.mykyta.zemlianyi.university.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import ua.foxminded.mykyta.zemlianyi.university.dao.AdminDao;
import ua.foxminded.mykyta.zemlianyi.university.dao.StudentDao;
import ua.foxminded.mykyta.zemlianyi.university.dao.TeacherDao;

@Deprecated
@Service
public class SampleDataGenerator {
    private AdminDao adminDao;
    private TeacherDao teacherDao;
    private StudentDao studentDao;
    private PasswordEncoder passwordEncoder;

    public SampleDataGenerator(AdminDao adminDao, TeacherDao teacherDao, StudentDao studentDao,
            PasswordEncoder passwordEncoder) {
        this.adminDao = adminDao;
        this.teacherDao = teacherDao;
        this.studentDao = studentDao;
        this.passwordEncoder = passwordEncoder;
    }

    // @PostConstruct
    public void encryptPasswords() {
        encryptAdminsPasswords();
        encryptTeachersPasswords();
        encryptStudentsPasswords();
    }

    private void encryptAdminsPasswords() {
        adminDao.findAll().forEach(admin -> {
            String rawPassword = admin.getPassword();
            admin.setPassword(passwordEncoder.encode(rawPassword));
            adminDao.save(admin);
        });
    }

    private void encryptTeachersPasswords() {
        teacherDao.findAll().forEach(teacher -> {
            String rawPassword = teacher.getPassword();
            teacher.setPassword(passwordEncoder.encode(rawPassword));
            teacherDao.save(teacher);
        });
    }

    private void encryptStudentsPasswords() {
        studentDao.findAll().forEach(student -> {
            String rawPassword = student.getPassword();
            student.setPassword(passwordEncoder.encode(rawPassword));
            studentDao.save(student);
        });
    }
}
