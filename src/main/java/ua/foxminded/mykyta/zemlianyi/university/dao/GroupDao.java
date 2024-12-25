package ua.foxminded.mykyta.zemlianyi.university.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.foxminded.mykyta.zemlianyi.university.dto.Group;

public interface GroupDao extends JpaRepository<Group, Long> {

}
