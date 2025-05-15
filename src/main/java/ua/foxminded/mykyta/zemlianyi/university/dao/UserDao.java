package ua.foxminded.mykyta.zemlianyi.university.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import ua.foxminded.mykyta.zemlianyi.university.dto.User;

@NoRepositoryBean
public interface UserDao<T extends User> extends JpaRepository<T, Long> {
    boolean existsByEmail(String email);

    Optional<T> findByEmail(String email);

    Page<T> findAll(Pageable page);

    List<T> findAll();
}
