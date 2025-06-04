package ua.foxminded.mykyta.zemlianyi.university.service;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import ua.foxminded.mykyta.zemlianyi.university.Constants;
import ua.foxminded.mykyta.zemlianyi.university.dao.UserDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.User;

public abstract class UserServiceImpl<T extends User> implements UserService<T> {
    private static Logger logger = LogManager.getLogger(UserServiceImpl.class.getName());
    protected UserDao<T> dao;
    protected PasswordEncoder passwordEncoder;

    protected UserServiceImpl(UserDao<T> dao, PasswordEncoder passwordEncoder) {
        this.dao = dao;
        this.passwordEncoder = passwordEncoder;
    }

    public T addNew(T user) {
        ObjectChecker.checkNullAndVerify(user);
        uniqueEmailOrThrow(user.getEmail());

        encodePasswordBeforeSave(user);

        logger.info("Adding new {} - {}", user.getClass().getSimpleName(), user);
        return dao.save(user);
    }

    protected abstract void uniqueEmailOrThrow(String email);

    @Transactional
    public T update(T user) {
        ObjectChecker.checkNullAndId(user);
        T mergedUser = mergeWithExisting(user);
        ObjectChecker.checkNullAndVerify(mergedUser);
        logger.info("Updating {} - {}", mergedUser.getClass().getSimpleName(), user);
        return dao.save(mergedUser);
    }

    protected void encodePasswordBeforeSave(T user) {
        logger.info("Encrypting password for user - {}", user);
        String rawPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(rawPassword));
    }

    protected abstract T mergeWithExisting(T user);

    public void delete(T user) {
        ObjectChecker.checkNullAndVerify(user);
        ObjectChecker.checkIfExistsInDb(user, dao);

        logger.info("Deleting {} - {}", user.getClass().getSimpleName(), user);
        dao.delete(user);
    }

    @Override
    public void deleteOrThrow(Long id) {
        T user = getByIdOrThrow(id);
        logger.info("Deleting {} - {}", user.getClass().getSimpleName(), user);
        dao.delete(user);
    }

    public T changePassword(T user) {
        ObjectChecker.checkNullAndVerify(user);

        Optional<T> managedAdminOptional = dao.findById(user.getId());

        if (managedAdminOptional.isPresent()) {
            T managedUser = managedAdminOptional.get();
            managedUser.setPassword(user.getPassword());
            logger.info("Changed password for {} - {}", user.getClass().getSimpleName(), user);
            encodePasswordBeforeSave(managedUser);
            return dao.save(managedUser);
        } else {
            throw new IllegalArgumentException(Constants.USER_PASSWORD_CHANGE_ERROR);
        }
    }

    public Page<T> findAll(Pageable page) {
        return dao.findAll(page);
    }

    public Optional<T> findById(Long id) {
        return dao.findById(id);
    }

    public List<T> findAll() {
        return dao.findAll();
    }

}
