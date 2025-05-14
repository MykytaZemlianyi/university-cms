package ua.foxminded.mykyta.zemlianyi.university.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.foxminded.mykyta.zemlianyi.university.Constants;
import ua.foxminded.mykyta.zemlianyi.university.dao.TeacherDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Teacher;

@Service
public class TeacherServiceImpl extends UserServiceImpl<Teacher> implements TeacherService {
    private static Logger logger = LogManager.getLogger(TeacherServiceImpl.class.getName());

    public TeacherServiceImpl(TeacherDao teacherDao, PasswordEncoder passwordEncoder) {
        super(teacherDao, passwordEncoder);
    }

    @Override
    protected Teacher mergeWithExisting(Teacher newTeacher) {
        ObjectChecker.checkNull(newTeacher);
        Optional<Teacher> existingTeacherOpt = dao.findById(newTeacher.getId());

        if (existingTeacherOpt.isPresent()) {
            Teacher existingTeacher = existingTeacherOpt.get();

            existingTeacher.setName(newTeacher.getName());
            existingTeacher.setSurname(newTeacher.getSurname());
            existingTeacher.setEmail(newTeacher.getEmail());
            existingTeacher.setPassword(choosePassword(newTeacher.getPassword(), existingTeacher.getPassword()));

            existingTeacher.setCourses(newTeacher.getCourses());

            return existingTeacher;
        } else {
            throw new IllegalArgumentException(Constants.USER_NOT_FOUND_ERROR);
        }
    }

    private String choosePassword(String newPassword, String existingPassword) {
        if (newPassword == null || newPassword.isBlank() || passwordEncoder.matches(newPassword, existingPassword)) {
            return existingPassword;
        } else {
            return passwordEncoder.encode(newPassword);
        }
    }

    @Override
    @Transactional
    public void delete(Teacher user) {
        ObjectChecker.checkNullAndVerify(user);
        ObjectChecker.checkIfExistsInDb(user, dao);

        logger.info("Deleting {} - {}", user.getClass().getSimpleName(), user);
        clearRelationsBeforeDeletion(user);
        dao.delete(user);
    }

    private void clearRelationsBeforeDeletion(Teacher user) {
        user.clearCourses();
        update(user);
    }

}
