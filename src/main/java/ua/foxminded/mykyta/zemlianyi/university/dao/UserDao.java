package ua.foxminded.mykyta.zemlianyi.university.dao;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import ua.foxminded.mykyta.zemlianyi.university.dto.User;

public interface UserDao<T extends User> extends CrudRepository<T, Long> {
    boolean existsByEmail(String email);

    Optional<T> findByEmail(String email);

    Page<T> findAll(Pageable page);
}
