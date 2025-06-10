package ua.foxminded.mykyta.zemlianyi.university.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ua.foxminded.mykyta.zemlianyi.university.dao.StudentDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Student;
import ua.foxminded.mykyta.zemlianyi.university.exceptions.StudentDuplicateException;
import ua.foxminded.mykyta.zemlianyi.university.exceptions.StudentNotFoundException;

@Service
public class StudentServiceImpl extends UserServiceImpl<Student> implements StudentService {
    private static Logger logger = LogManager.getLogger(StudentServiceImpl.class.getName());

    public StudentServiceImpl(StudentDao studentDao, PasswordEncoder passwordEncoder) {
        super(studentDao, passwordEncoder);
    }

    @Override
    protected Student mergeWithExisting(Student newStudent) {
        ObjectChecker.checkNullAndId(newStudent);
        Student existingStudent = getByIdOrThrow(newStudent.getId());

        existingStudent.setName(newStudent.getName());
        existingStudent.setSurname(newStudent.getSurname());
        existingStudent.setEmail(newStudent.getEmail());
        existingStudent.setPassword(choosePassword(newStudent.getPassword(), existingStudent.getPassword()));

        if (newStudent.getGroup() == null || newStudent.getGroup().getId() == null) {
            existingStudent.setGroup(null);
        } else {
            existingStudent.setGroup(newStudent.getGroup());
        }

        return existingStudent;

    }

    @Override
    public Student getByIdOrThrow(Long id) {
        return dao.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
    }

    private String choosePassword(String newPassword, String existingPassword) {
        if (newPassword == null || newPassword.isBlank() || passwordEncoder.matches(newPassword, existingPassword)) {
            return existingPassword;
        } else {
            return passwordEncoder.encode(newPassword);
        }
    }

    @Override
    protected void uniqueEmailOrThrow(String email) {
        if (dao.existsByEmail(email)) {
            throw new StudentDuplicateException(email);
        }
    }

}
