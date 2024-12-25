package ua.foxminded.mykyta.zemlianyi.university.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.foxminded.mykyta.zemlianyi.university.dto.Admin;

public interface AdminDao extends JpaRepository<Admin, Long> {

}
