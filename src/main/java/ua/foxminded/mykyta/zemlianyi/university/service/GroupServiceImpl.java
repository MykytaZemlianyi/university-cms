package ua.foxminded.mykyta.zemlianyi.university.service;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ua.foxminded.mykyta.zemlianyi.university.Constants;
import ua.foxminded.mykyta.zemlianyi.university.dao.GroupDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Group;
import ua.foxminded.mykyta.zemlianyi.university.dto.Student;

@Service
public class GroupServiceImpl implements GroupService {
    private static Logger logger = LogManager.getLogger(GroupServiceImpl.class.getName());

    private GroupDao groupDao;

    public GroupServiceImpl(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    @Override
    public Group addNew(Group group) {
        ObjectChecker.checkNullAndVerify(group);
        if (groupDao.existsByName(group.getName())) {
            throw new IllegalArgumentException(group.getName() + Constants.GROUP_ADD_NEW_ERROR_EXISTS_BY_NAME);
        }
        logger.info("Adding new group - {}", group);
        return groupDao.save(group);
    }

    @Override
    public Group update(Group group) {
        ObjectChecker.checkNullAndVerify(group);
        ObjectChecker.checkIfExistsInDb(group, groupDao);

        logger.info("Updating group - {}", group);
        return groupDao.save(group);
    }

    @Override
    public void delete(Group group) {
        ObjectChecker.checkNullAndVerify(group);
        ObjectChecker.checkIfExistsInDb(group, groupDao);

        logger.info("Updating group - {}", group);
        groupDao.delete(group);
    }

    @Override
    public Group findForStudent(Student student) {
        if (student == null || student.getId() == null) {
            throw new IllegalArgumentException("Student" + Constants.USER_INVALID);
        }
        logger.info("Looking for Group for student - {}", student);
        return groupDao.findByStudents(student);
    }

    @Override
    public Page<Group> findAll(Pageable pageable) {
        return groupDao.findAll(pageable);
    }

    @Override
    public List<Group> findAll() {
        return groupDao.findAll();
    }

    @Override
    public Optional<Group> findById(Long groupId) {
        // TODO Auto-generated method stub
        return groupDao.findById(groupId);
    }

}
