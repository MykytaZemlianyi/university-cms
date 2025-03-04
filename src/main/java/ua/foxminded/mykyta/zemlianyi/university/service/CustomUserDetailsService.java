package ua.foxminded.mykyta.zemlianyi.university.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ua.foxminded.mykyta.zemlianyi.university.Constants;
import ua.foxminded.mykyta.zemlianyi.university.dao.AdminDao;
import ua.foxminded.mykyta.zemlianyi.university.dao.StudentDao;
import ua.foxminded.mykyta.zemlianyi.university.dao.TeacherDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Admin;
import ua.foxminded.mykyta.zemlianyi.university.dto.Student;
import ua.foxminded.mykyta.zemlianyi.university.dto.Teacher;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private static Logger logger = LogManager.getLogger(CustomUserDetailsService.class.getName());
    private AdminDao adminDao;
    private TeacherDao teacherDao;
    private StudentDao studentDao;

    public CustomUserDetailsService(AdminDao adminDao, TeacherDao teacherDao, StudentDao studentDao) {
        this.adminDao = adminDao;
        this.studentDao = studentDao;
        this.teacherDao = teacherDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Admin> admin = adminDao.findByEmail(username);
        if (admin.isPresent()) {
            logger.info("User found and identified as Admin");
            UserDetails userDetails = User.withUsername(admin.get().getEmail()).password(admin.get().getPassword())
                    .authorities(Constants.ROLE_PREFIX + Constants.ROLE_ADMIN).build();

            logger.info("UserDetails loaded: {}", userDetails.getUsername());
            logger.info("User authorities: {}", userDetails.getAuthorities());
            return userDetails;

        }
        logger.error("User NOT found in Admin table");

        Optional<Teacher> teacher = teacherDao.findByEmail(username);
        if (teacher.isPresent()) {
            logger.info("User found and identified as Teacher");
            return User.withUsername(teacher.get().getEmail()).password(teacher.get().getPassword())
                    .authorities(Constants.ROLE_PREFIX + Constants.ROLE_TEACHER).build();
        }
        logger.error("User NOT found in Teacher table");

        Optional<Student> student = studentDao.findByEmail(username);
        if (student.isPresent()) {
            logger.info("User found and identified as Student");
            return User.withUsername(student.get().getEmail()).password(student.get().getPassword())
                    .authorities(Constants.ROLE_PREFIX + Constants.ROLE_STUDENT).build();
        }
        logger.error("User NOT found in Student table");

        throw new UsernameNotFoundException(Constants.USER_NOT_FOUND_ERROR);
    }
}
