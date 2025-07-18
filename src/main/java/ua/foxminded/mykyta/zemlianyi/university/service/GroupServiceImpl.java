package ua.foxminded.mykyta.zemlianyi.university.service;

import java.util.ArrayList;
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
import ua.foxminded.mykyta.zemlianyi.university.exceptions.GroupDuplicateException;
import ua.foxminded.mykyta.zemlianyi.university.exceptions.GroupNotFoundException;

@Service
public class GroupServiceImpl implements GroupService {
    private static Logger logger = LogManager.getLogger(GroupServiceImpl.class.getName());

    private GroupDao groupDao;
    private StudentDao studentDao;
    private StudentService studentService;

    public GroupServiceImpl(GroupDao groupDao, StudentDao studentDao, StudentService studentService) {
        this.groupDao = groupDao;
        this.studentDao = studentDao;
        this.studentService = studentService;
    }

    @Override
    @Transactional
    public Group addNew(Group group) {
        ObjectChecker.checkNullAndVerify(group);
        Set<Student> students = group.getStudents();
        Group savedGroup = saveGroupWithoutStudents(group);

        updateStudentsReferences(savedGroup, students);
        return savedGroup;
    }

    private void updateStudentsReferences(Group savedGroup, Set<Student> students) {
        for (Student student : students) {
            if (student.getId() == null) {
                throw new IllegalArgumentException("Student ID cannot be null");
            }
            Student managedStudent = studentDao.getReferenceById(student.getId());
            managedStudent.setGroup(savedGroup);
            savedGroup.getStudents().add(managedStudent);
        }

    }

    private Group saveGroupWithoutStudents(Group group) {
        ObjectChecker.checkNullAndVerify(group);
        if (groupDao.existsByName(group.getName())) {
            throw new GroupDuplicateException(group.getName());
        }
        group.clearStudents();
        logger.info("Adding new group without students - {}", group);
        return groupDao.save(group);
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
        ObjectChecker.checkNullAndId(newGroup);
        Group existingGroup = getByIdOrThrow(newGroup.getId());

        existingGroup.setName(newGroup.getName());
        existingGroup.assignStudents(newGroup.getStudents());
        existingGroup.setCourses(newGroup.getCourses());

        return existingGroup;

    }

    @Override
    public void delete(Group group) {
        ObjectChecker.checkNullAndVerify(group);
        ObjectChecker.checkIfExistsInDb(group, groupDao);
        logger.info("Updating group - {}", group);
        groupDao.deleteById(group.getId());
    }

    @Override
    public void deleteOrThrow(Long id) {
        Group group = getByIdOrThrow(id);
        logger.info("Deleting group - {}", group);
        groupDao.delete(group);
    }

    @Override
    public Group findForStudent(Student student) {
        ObjectChecker.checkNullAndId(student);
        logger.info("Looking for Group for student - {}", student);
        return groupDao.findByStudents(student);
    }

    private List<Group> findForStduent(Student student) {
        // return single group in a list to integrate with existing UI group table
        // fragment. Returns empty list if no group found.
        return Optional.ofNullable(groupDao.findByStudents(student)).map(List::of).orElseGet(ArrayList::new);
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

    @Override
    public Group getByIdOrThrow(Long id) {
        return groupDao.findById(id).orElseThrow(() -> new GroupNotFoundException(id));
    }

    @Override
    public List<Group> findForUserWithUsername(String username, String role) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException(Constants.USERNAME_INVALID);
        }
        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException(Constants.ROLE_INVALID);
        }
        List<Group> groups;

        if (role.equals(Constants.ROLE_STUDENT)) {
            Student student = studentService.getByEmailOrThrow(username);
            groups = findForStduent(student);
        } else {
            throw new IllegalArgumentException(role + Constants.INVALID_OPERATION_FOR_ROLE);
        }

        return groups;
    }

}
