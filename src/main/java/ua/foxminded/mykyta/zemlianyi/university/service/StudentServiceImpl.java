package ua.foxminded.mykyta.zemlianyi.university.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ua.foxminded.mykyta.zemlianyi.university.Constants;
import ua.foxminded.mykyta.zemlianyi.university.dao.StudentDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Student;

@Service
public class StudentServiceImpl extends UserServiceImpl<Student> implements StudentService {
    private static Logger logger = LogManager.getLogger(StudentServiceImpl.class.getName());

    public StudentServiceImpl(StudentDao studentDao, PasswordEncoder passwordEncoder) {
        super(studentDao, passwordEncoder);
    }

    @Override
    protected Student mergeWithExisting(Student newStudent) {
        ObjectChecker.checkNull(newStudent);
        Optional<Student> existingStudentOpt = dao.findById(newStudent.getId());

        if (existingStudentOpt.isPresent()) {
            Student existingStudent = existingStudentOpt.get();

            existingStudent.setName(chooseString(newStudent.getName(), existingStudent.getName()));
            existingStudent.setSurname(chooseString(newStudent.getSurname(), existingStudent.getSurname()));
            existingStudent.setEmail(chooseString(newStudent.getEmail(), existingStudent.getEmail()));
            existingStudent.setPassword(choosePassword(newStudent.getPassword(), existingStudent.getPassword()));

            existingStudent.setGroup(newStudent.getGroup());

            return existingStudent;

        } else {
            throw new IllegalArgumentException(Constants.USER_NOT_FOUND_ERROR);
        }
    }

    private String chooseString(String newString, String existingString) {
        if (newString == null || newString.isBlank() || newString.equals(existingString)) {
            return existingString;
        } else {
            return newString;
        }
    }

    private String choosePassword(String newPassword, String existingPassword) {
        if (newPassword == null || newPassword.isBlank() ||passwordEncoder.matches(newPassword, existingPassword)) {
            return existingPassword;
        } else {
            return passwordEncoder.encode(newPassword);
        }
    }

}
