package ua.foxminded.mykyta.zemlianyi.university.service;

import java.util.List;
import java.util.Optional;

import ua.foxminded.mykyta.zemlianyi.university.dto.User;

public interface UserService<T extends User> {
    public T addNew(T user);

    public T update(T user);

    public void delete(T user);

    public void deleteByIdOrThrow(Long id);

    public T changePassword(T user);

    public Optional<T> findById(Long id);

    public T getByIdOrThrow(Long id);
    
    List<T> findAll();
}
