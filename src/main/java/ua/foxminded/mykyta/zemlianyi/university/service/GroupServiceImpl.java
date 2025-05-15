package ua.foxminded.mykyta.zemlianyi.university.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.foxminded.mykyta.zemlianyi.university.Constants;
import ua.foxminded.mykyta.zemlianyi.university.dao.GroupDao;
import ua.foxminded.mykyta.zemlianyi.university.dao.StudentDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Group;
import ua.foxminded.mykyta.zemlianyi.university.dto.Student;

@Service
public class GroupServiceImpl implements GroupService {
    private static Logger logger = LogManager.getLogger(GroupServiceImpl.class.getName());

    private GroupDao groupDao;
    private StudentDao studentDao;

    public GroupServiceImpl(GroupDao groupDao, StudentDao studentDao) {
        this.groupDao = groupDao;
        this.studentDao = studentDao;
    }

    @Override
    @Transactional
    public Group addNew(Group group) {
        ObjectChecker.checkNullAndVerify(group);
        if (groupDao.existsByName(group.getName())) {
            throw new IllegalArgumentException(group.getName() + Constants.GROUP_ADD_NEW_ERROR_EXISTS_BY_NAME);
        }
        logger.info("Adding new group - {}", group);
        getStudentsReferencesBeforeSave(group);
        return groupDao.save(group);
    }

    private void getStudentsReferencesBeforeSave(Group group) {
        Set<Student> managedStudents = new HashSet<>();
        for (Student student : group.getStudents()) {
            managedStudents.add(studentDao.getReferenceById(student.getId()));
        }
        group.setStudents(managedStudents);
    }

    @Override
    @Transactional
    public Group update(Group group) {
        ObjectChecker.checkNullAndVerify(group);
        Group mergedGroup = mergeWithExisting(group);
        logger.info("Updating group - {}", group);
        return groupDao.save(mergedGroup);
    }

    private Group mergeWithExisting(Group newGroup) {
        ObjectChecker.checkNull(newGroup);
        Optional<Group> existingCourseOpt = groupDao.findById(newGroup.getId());

        if (existingCourseOpt.isPresent()) {
            Group existingGroup = existingCourseOpt.get();

            existingGroup.setName(newGroup.getName());
            existingGroup.setStudents(newGroup.getStudents());
            existingGroup.setCourses(newGroup.getCourses());

            return existingGroup;

        } else {
            throw new IllegalArgumentException(Constants.OBJECT_UPDATE_FAIL_DOES_NOT_EXIST);
        }
    }

    @Override
    public void delete(Group group) {
        ObjectChecker.checkNullAndVerify(group);
        ObjectChecker.checkIfExistsInDb(group, groupDao);
        logger.info("Updating group - {}", group);
        groupDao.deleteById(group.getId());
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
        return groupDao.findById(groupId);
    }

}
