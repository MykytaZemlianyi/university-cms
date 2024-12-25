package ua.foxminded.mykyta.zemlianyi.university.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.foxminded.mykyta.zemlianyi.university.dto.Student;

public interface StudentDao extends JpaRepository<Student, Long> {

}
