package ua.foxminded.mykyta.zemlianyi.university.dao;

import org.springframework.stereotype.Repository;

import ua.foxminded.mykyta.zemlianyi.university.dto.Student;

@Repository
public interface StudentDao extends UserDao<Student> {

}
