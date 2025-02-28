package ua.foxminded.mykyta.zemlianyi.university.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import ua.foxminded.mykyta.zemlianyi.university.Constants;
import ua.foxminded.mykyta.zemlianyi.university.dao.UserDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.User;

public abstract class UserServiceImpl<T extends User> implements UserService<T> {
    private static Logger logger = LogManager.getLogger(UserServiceImpl.class.getName());
    CrudRepository<T, Long> dao;
    private PasswordEncoder passwordEncoder;

    protected UserServiceImpl(CrudRepository<T, Long> dao, PasswordEncoder passwordEncoder) {
        this.dao = dao;
        this.passwordEncoder = passwordEncoder;
    }

    public T addNew(T user) {
        ObjectChecker.check(user);
        if (dao instanceof UserDao userDao && userDao.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException(user.getEmail() + Constants.USER_SAVE_ERROR_EMAIL_EXISTS);
        }

        encodePasswordBeforeSave(user);

        logger.info("Adding new {} - {}", user.getClass().getSimpleName(), user);
        return dao.save(user);
    }

    private void encodePasswordBeforeSave(T user) {
        logger.info("Encrypting password for user - {}", user);
        String rawPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(rawPassword));
    }

    public T update(T user) {
        ObjectChecker.check(user);
        ObjectChecker.checkIfExistsInDb(user, dao);
        logger.info("Updating {} - {}", user.getClass().getSimpleName(), user);
        return dao.save(user);
    }

    public void delete(T user) {
        ObjectChecker.check(user);
        ObjectChecker.checkIfExistsInDb(user, dao);

        logger.info("Deleting {} - {}", user.getClass().getSimpleName(), user);
        dao.delete(user);
    }

    public T changePassword(T user) {
        ObjectChecker.check(user);

        Optional<T> managedAdminOptional = dao.findById(user.getId());

        if (managedAdminOptional.isPresent()) {
            T managedAdmin = managedAdminOptional.get();
            managedAdmin.setPassword(user.getPassword());
            logger.info("Changed password for {} - {}", user.getClass().getSimpleName(), user);
            return dao.save(managedAdmin);
        } else {
            throw new IllegalArgumentException(Constants.USER_PASSWORD_CHANGE_ERROR);
        }
    }

}
