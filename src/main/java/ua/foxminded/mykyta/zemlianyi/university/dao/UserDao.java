package ua.foxminded.mykyta.zemlianyi.university.dao;

import java.util.Optional;

import ua.foxminded.mykyta.zemlianyi.university.dto.User;

public interface UserDao<T extends User> {
    boolean existsByEmail(String email);

    Optional<T> findByEmail(String email);
}
