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
        resolveCustomFields(user);
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

    /**
     * Merges the fields of the new user with the existing user in the database.
     * This method retrieves the existing user by ID, merges the default and custom
     * fields, and returns the updated user.
     *
     * @param user the user with updated fields
     * @return the merged user
     */
    protected T mergeWithExisting(T user) {
        ObjectChecker.checkNullAndId(user);
        T existingUser = getByIdOrThrow(user.getId());
        mergeDefaultFields(existingUser, user);
        mergeCustomFields(existingUser, user);
        return existingUser;
    }

    /**
     * Merges default fields from newUser into existingUser.
     *
     * @param existingUser the user that already exists in the database
     * @param newUser      the user with updated fields
     */
    protected void mergeDefaultFields(T existingUser, T newUser) {
        existingUser.setName(newUser.getName());
        existingUser.setSurname(newUser.getSurname());
        existingUser.setEmail(newUser.getEmail());
        existingUser.setPassword(choosePassword(newUser.getPassword(), existingUser.getPassword()));
    }

    /**
     * Merges custom fields from newUser into existingUser. This method can be
     * overridden in subclasses to handle specific fields.
     *
     * @param existingUser the user that already exists in the database
     * @param newUser      the user with updated fields
     */
    protected abstract void mergeCustomFields(T existingUser, T newUser);

    /**
     * Resolves custom fields for the user. This method can be overridden in
     * subclasses to handle specific fields.
     *
     * @param user the user for which custom fields need to be resolved
     */
    protected abstract void resolveCustomFields(T user);

    private String choosePassword(String newPassword, String existingPassword) {
        if (newPassword == null || newPassword.isBlank() || passwordEncoder.matches(newPassword, existingPassword)) {
            return existingPassword;
        } else {
            return passwordEncoder.encode(newPassword);
        }
    }

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

    public T getByEmailOrThrow(String email) {
        return dao.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException(Constants.USER_NOT_FOUND_ERROR + " with email: " + email));
    }

    public List<T> findAll() {
        return dao.findAll();
    }

}
