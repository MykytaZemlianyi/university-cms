package ua.foxminded.mykyta.zemlianyi.university.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.repository.CrudRepository;

import ua.foxminded.mykyta.zemlianyi.university.Constants;
import ua.foxminded.mykyta.zemlianyi.university.dto.User;

public abstract class UserService<T extends User> {
    private static Logger logger = LogManager.getLogger(UserService.class.getName());
    CrudRepository<T, Long> dao;

    public UserService(CrudRepository<T, Long> dao) {
        this.dao = dao;
    }

    public T addNew(T user) {
        verifyObject(user);
        logger.info("Adding new {} - {}", user.getClass().getSimpleName(), user);
        return dao.save(user);
    }

    public T update(T user) {
        verifyObject(user);
        if (dao.existsById(user.getId())) {
            logger.info("Updating {} - {}", user.getClass().getSimpleName(), user);
            return dao.save(user);
        } else {
            throw new IllegalArgumentException(Constants.USER_UPDATE_FAIL_DOES_NOT_EXIST);
        }
    }

    public T changePassword(T user) {
        verifyObject(user);

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

    private void verifyObject(T user) {
        if (user == null) {
            throw new IllegalArgumentException(Constants.USER_NULL);
        } else if (!user.verify()) {
            throw new IllegalArgumentException(user.getClass().getSimpleName() + Constants.USER_INVALID);
        }
    }
}
