package ua.foxminded.mykyta.zemlianyi.university.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ua.foxminded.mykyta.zemlianyi.university.Constants;
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
    protected Student mergeWithExisting(Student newStudent) {
        ObjectChecker.checkNull(newStudent);
        Optional<Student> existingStudentOpt = dao.findById(newStudent.getId());

        if (existingStudentOpt.isPresent()) {
            Student existingStudent = existingStudentOpt.get();
            Student mergedStudent = new Student();

            mergedStudent.setId(existingStudent.getId());
            mergedStudent.setName(chooseString(newStudent.getName(), existingStudent.getName()));
            mergedStudent.setSurname(chooseString(newStudent.getSurname(), existingStudent.getSurname()));
            mergedStudent.setEmail(chooseString(newStudent.getEmail(), existingStudent.getEmail()));
            mergedStudent.setGroup(newStudent.getGroup());
            mergedStudent.setPassword(choosePassword(newStudent.getPassword(), existingStudent.getPassword()));

            return mergedStudent;

        } else {
            throw new IllegalArgumentException(Constants.USER_NOT_FOUND_ERROR);
        }
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
