package ua.foxminded.mykyta.zemlianyi.university.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.foxminded.mykyta.zemlianyi.university.dto.Group;
@Repository
public interface GroupDao extends JpaRepository<Group, Long> {

}
