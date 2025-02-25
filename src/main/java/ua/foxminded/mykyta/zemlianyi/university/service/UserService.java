package ua.foxminded.mykyta.zemlianyi.university.service;

import ua.foxminded.mykyta.zemlianyi.university.dto.User;

public interface UserService<T extends User> {
    public T addNew(T user);

    public T update(T user);

    public void delete(T user);

    public T changePassword(T user);
}
