package ua.foxminded.mykyta.zemlianyi.university.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import ua.foxminded.mykyta.zemlianyi.university.dao.GroupDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Group;
import ua.foxminded.mykyta.zemlianyi.university.dto.Student;

@SpringBootTest(classes = { GroupServiceImpl.class })
class GroupServiceImplTest {
    @MockitoBean
    GroupDao groupDao;

    @Autowired
    GroupService groupService;

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
        assertThrows(IllegalArgumentException.class, () -> {
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

        assertThrows(IllegalArgumentException.class, () -> {
            groupService.update(untrackedGroup);
        });
    }

    @Test
    void update_shouldUpdateGroup_whenGroupValidAndSavedBeforeUpdate() {
        Group trackedGroup = new Group();
        trackedGroup.setId(1L);
        trackedGroup.setName("Group");

        when(groupDao.existsById(1L)).thenReturn(true);

        groupService.update(trackedGroup);

        verify(groupDao).save(trackedGroup);
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

        verify(groupDao).delete(group);
    }
}
