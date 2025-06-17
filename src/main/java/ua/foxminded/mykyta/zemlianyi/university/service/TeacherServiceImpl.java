package ua.foxminded.mykyta.zemlianyi.university.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ua.foxminded.mykyta.zemlianyi.university.dao.TeacherDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Teacher;
import ua.foxminded.mykyta.zemlianyi.university.exceptions.TeacherDuplicateException;
import ua.foxminded.mykyta.zemlianyi.university.exceptions.TeacherNotFoundException;

@Service
public class TeacherServiceImpl extends UserServiceImpl<Teacher> implements TeacherService {
    private static Logger logger = LogManager.getLogger(TeacherServiceImpl.class.getName());

    public TeacherServiceImpl(TeacherDao teacherDao, PasswordEncoder passwordEncoder) {
        super(teacherDao, passwordEncoder);
    }

    @Override
    protected void mergeCustomFields(Teacher existingTeacher, Teacher newTeacher) {
        existingTeacher.setCourses(newTeacher.getCourses());
    }

    @Override
    public void delete(Teacher user) {
        ObjectChecker.checkNullAndVerify(user);
        ObjectChecker.checkIfExistsInDb(user, dao);

        logger.info("Deleting {} - {}", user.getClass().getSimpleName(), user);
        dao.deleteById(user.getId());
    }

    @Override
    public Teacher getByIdOrThrow(Long id) {

        return dao.findById(id).orElseThrow(() -> new TeacherNotFoundException(id));
    }

    @Override
    protected void uniqueEmailOrThrow(String email) {
        if (dao.existsByEmail(email)) {
            throw new TeacherDuplicateException(email);
        }
    }

    @Override
    protected void resolveCustomFields(Teacher user) {
        // No custom fields to resolve for Teacher
    }

}
