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
    protected void mergeCustomFields(Student existingStudent, Student newStudent) {
        if (newStudent.getGroup() == null || newStudent.getGroup().getId() == null) {
            existingStudent.setGroup(null);
        } else {
            existingStudent.setGroup(newStudent.getGroup());
        }
    }

    @Override
    protected void resolveCustomFields(Student student) {
        if (student.getGroup() == null || student.getGroup().getId() == null) {
            student.setGroup(null);
        }
    }

    @Override
    public Student getByIdOrThrow(Long id) {
        return dao.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
    }

    @Override
    protected void uniqueEmailOrThrow(String email) {
        if (dao.existsByEmail(email)) {
            throw new StudentDuplicateException(email);
        }
    }

}
