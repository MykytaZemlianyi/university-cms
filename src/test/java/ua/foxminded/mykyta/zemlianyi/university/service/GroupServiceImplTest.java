package ua.foxminded.mykyta.zemlianyi.university.service;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import ua.foxminded.mykyta.zemlianyi.university.dao.GroupDao;
import ua.foxminded.mykyta.zemlianyi.university.dao.StudentDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Group;
import ua.foxminded.mykyta.zemlianyi.university.dto.Student;
import ua.foxminded.mykyta.zemlianyi.university.exceptions.GroupDuplicateException;
import ua.foxminded.mykyta.zemlianyi.university.exceptions.GroupNotFoundException;

@SpringBootTest(classes = { GroupServiceImpl.class })
class GroupServiceImplTest {

    @MockitoBean
    GroupDao groupDao;

    @MockitoBean
    StudentDao studentDao;

    @MockitoBean
    StudentService studentService;

    @MockitoBean
    PasswordEncoder encoder;

    @Autowired
    GroupServiceImpl groupService;

    @Test
    void addNew_shouldThrowIllegalArgumentException_whenGroupIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            groupService.addNew(null);
        });
    }

    @Test
    void addNew_shouldThrowIllegalArgumentException_whenGroupIsInvalid() {
        Group invalidGroup = new Group();

        assertThrows(IllegalArgumentException.class, () -> {
            groupService.addNew(invalidGroup);
        });
    }

    @Test
    void addNew_shouldThrowIllegalArgumentException_whenGroupWithSameNameExists() {
        Group groupWithSameName = new Group();
        groupWithSameName.setName("AA-11");

        doReturn(true).when(groupDao).existsByName(groupWithSameName.getName());
        assertThrows(GroupDuplicateException.class, () -> {
            groupService.addNew(groupWithSameName);
        });
    }

    @Test
    void addNew_shouldSaveGroup_whenGroupIsValid() {
        Group group = new Group();
        group.setName("Valid Group");

        groupService.addNew(group);

        verify(groupDao).save(group);
    }

    @Test
    void update_shouldThrowIllegalArgumentException_whenGroupIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            groupService.update(null);
        });
    }

    @Test
    void update_shouldThrowIllegalArgumentException_whenGroupIsInvalid() {
        Group invalidGroup = new Group();

        assertThrows(IllegalArgumentException.class, () -> {
            groupService.update(invalidGroup);
        });
    }

    @Test
    void update_shouldThrowIllegalArgumentException_whenGroupDidNotSavedBeforeUpdate() {
        Group untrackedGroup = new Group();
        untrackedGroup.setId(1L);
        untrackedGroup.setName("Group");

        when(groupDao.existsById(1L)).thenReturn(false);

        assertThrows(GroupNotFoundException.class, () -> {
            groupService.update(untrackedGroup);
        });
    }

    @Test
    void update_shouldUpdateGroup_whenGroupValidAndSavedBeforeUpdate() {
        Group managedGroup = new Group();
        managedGroup.setId(1L);
        managedGroup.setName("old Group");

        Group updatedGroup = new Group();
        updatedGroup.setId(1L);
        updatedGroup.setName("Group");

        when(groupDao.findById(1L)).thenReturn(Optional.of(managedGroup));

        groupService.update(updatedGroup);

        verify(groupDao).save(updatedGroup);
    }

    @Test
    void findForStudent_shouldThrowIllegalArgumentException_whenStudentIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            groupService.findForStudent(null);
        });
    }

    @Test
    void findForStudent_shouldThrowIllegalArgumentException_whenStudentHasNoId() {
        Student student = new Student();

        assertThrows(IllegalArgumentException.class, () -> {
            groupService.findForStudent(student);
        });
    }

    @Test
    void findForStudent_shouldReturnNull_whenStudentNotInAnyGroup() {
        Student student = new Student();
        student.setId(1L);

        when(groupDao.findByStudents(student)).thenReturn(null);

        Group actualGroup = groupService.findForStudent(student);

        assertNull(actualGroup);
    }

    @Test
    void findForStudent_shouldReturnGroup_whenStudentIsValid() {
        Student student = new Student();
        student.setId(1L);
        Group expectedGroup = new Group();
        expectedGroup.setId(2L);
        expectedGroup.setName("Group A");

        when(groupDao.findByStudents(student)).thenReturn(expectedGroup);

        Group actualGroup = groupService.findForStudent(student);

        assertNotNull(actualGroup);
        assertEquals(expectedGroup, actualGroup);
    }

    @Test
    void delete_shouldThrowIllegalArgumentException_when_groupIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            groupService.delete(null);
        });
    }

    @Test
    void delete_shouldThrowIllegalArgumentException_when_groupIsInvalid() {
        Group invalidGroup = new Group();
        assertThrows(IllegalArgumentException.class, () -> {
            groupService.delete(invalidGroup);
        });
    }

    @Test
    void delete_shouldThrowIllegalArgumentException_when_groupIsNotSavedInDb() {
        Group group = new Group();
        group.setName("Valid Group");

        doReturn(false).when(groupDao).existsById(group.getId());
        assertThrows(IllegalArgumentException.class, () -> {
            groupService.delete(group);
        });
    }

    @Test
    void delete_shouldDeleteGroup_when_groupIsValidAndExistsInDb() {
        Group group = new Group();
        group.setName("Valid Group");

        doReturn(true).when(groupDao).existsById(group.getId());

        groupService.delete(group);

        verify(groupDao).deleteById(group.getId());
    }

    @ParameterizedTest
    @ValueSource(strings = { "ADMIN", "STUDENT", "TEACHER", "STAFF", "INVALID_ROLE" })
    void findForUserWithUsername_shouldThrowIllegalArgumentException_whenUsernameIsNull(String role) {
        assertThrows(IllegalArgumentException.class, () -> {
            groupService.findForUserWithUsername(null, role);
        });
    }

    @ParameterizedTest
    @ValueSource(strings = { "ADMIN", "STUDENT", "TEACHER", "STAFF", "INVALID_ROLE" })
    void findForUserWithUsername_shouldThrowIllegalArgumentException_whenUsernameIsBlank(String role) {
        assertThrows(IllegalArgumentException.class, () -> {
            groupService.findForUserWithUsername("", role);
        });
    }

    @Test
    void findForUserWithUsername_shouldThrowIllegalArgumentException_whenRoleIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            groupService.findForUserWithUsername("validUsername", null);
        });
    }

    @Test
    void findForUserWithUsername_shouldThrowIllegalArgumentException_whenRoleIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> {
            groupService.findForUserWithUsername("validUsername", "");
        });
    }

    @Test
    void findForUserWithUsername_shouldReturnListWithGroup_whenRoleIsStudent() {
        String username = "validUsername";
        String role = "STUDENT";

        Group group = new Group();
        group.setId(1L);
        group.setName("Group A");

        Student student = new Student();
        student.setId(1L);

        doReturn(student).when(studentService).getByEmailOrThrow(username);
        doReturn(group).when(groupDao).findByStudents(student);

        List<Group> expectedGroups = List.of(group);
        List<Group> actualGroups = groupService.findForUserWithUsername(username, role);

        assertArrayEquals(expectedGroups.toArray(), actualGroups.toArray());
    }

    @ParameterizedTest
    @ValueSource(strings = { "ADMIN", "TEACHER", "STAFF", "INVALID_ROLE" })
    void findForUserWithUsername_shouldThrowIllegalArgumentException_whenRoleIsNotStudent(String role) {
        String username = "validUsername";

        assertThrows(IllegalArgumentException.class, () -> {
            groupService.findForUserWithUsername(username, role);
        });
    }
}
