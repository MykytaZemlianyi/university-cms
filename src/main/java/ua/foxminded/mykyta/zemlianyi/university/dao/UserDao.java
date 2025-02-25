package ua.foxminded.mykyta.zemlianyi.university.dao;

public interface UserDao {
    boolean existsByEmail(String email);
}
