package ua.foxminded.mykyta.zemlianyi.university.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.foxminded.mykyta.zemlianyi.university.dto.Teacher;

public interface TeacherDao extends JpaRepository<Teacher, Long> {

}
