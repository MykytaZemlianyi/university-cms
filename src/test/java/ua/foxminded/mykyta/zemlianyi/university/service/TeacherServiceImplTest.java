package ua.foxminded.mykyta.zemlianyi.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import ua.foxminded.mykyta.zemlianyi.university.dao.TeacherDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Teacher;

@SpringBootTest(classes = { TeacherServiceImpl.class })
class TeacherServiceImplTest {
    @MockitoBean
    TeacherDao teacherDao;
    @Autowired
    TeacherService teacherService;

    Teacher teacher;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setName("Marek");
        teacher.setSurname("Szepski");
        teacher.setEmail("mszepski@gmail.com");
        teacher.setPassword("123456789");
    }

    @Test
    void addNew_shouldThrowIllegalArgumentException_whenTeacherIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            teacherService.addNew(null);
        });
    }

    @Test
    void addNew_shouldThrowIllegalArgumentException_whenTeacherIsNotVerified() {
        Teacher ivalidTeacher = new Teacher();
        assertThrows(IllegalArgumentException.class, () -> {
            teacherService.addNew(ivalidTeacher);
        });
    }

    @Test
    void addNew_shouldThrowIllegalArgumentException_whenTeacherWithSameEmailExists() {
        Teacher teacherWithSameEmail = new Teacher();
        teacherWithSameEmail.setId(2L);
        teacherWithSameEmail.setName("Marek");
        teacherWithSameEmail.setSurname("Szwepster");
        teacherWithSameEmail.setEmail(teacher.getEmail());
        teacherWithSameEmail.setPassword("securePass123");
        doReturn(true).when(teacherDao).existsByEmail(teacherWithSameEmail.getEmail());

        assertThrows(IllegalArgumentException.class, () -> {
            teacherService.addNew(teacherWithSameEmail);
        });
    }

    @Test
    void addNew_shouldSaveTeacher_whenTeacherIsVerified() {

        teacherService.addNew(teacher);

        verify(teacherDao).save(teacher);
    }

    @Test
    void update_shouldThrowIllegalArgumentException_whenTeacherIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            teacherService.update(null);
        });
    }

    @Test
    void update_shouldThrowIllegalArgumentException_whenTeacherIsNotVerified() {
        Teacher ivalidTeacher = new Teacher();
        assertThrows(IllegalArgumentException.class, () -> {
            teacherService.update(ivalidTeacher);
        });
    }

    @Test
    void update_shouldThrowIllegalArgumentException_whenTeacherDoesNotExistsInDb() {
        doReturn(false).when(teacherDao).existsById(teacher.getId());
        assertThrows(IllegalArgumentException.class, () -> {
            teacherService.update(teacher);
        });
    }

    @Test
    void update_shouldUpdate_whenTeacherIsCorrectAndExistsInDb() {
        doReturn(true).when(teacherDao).existsById(teacher.getId());

        teacherService.update(teacher);

        verify(teacherDao).save(teacher);
    }

    @Test
    void changePassword_shouldThrowIllegalArgumentException_whenTeacherIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            teacherService.changePassword(null);
        });
    }

    @Test
    void changePassword_shouldThrowIllegalArgumentException_whenTeacherIsInvalid() {
        Teacher invalidTeacher = new Teacher();
        assertThrows(IllegalArgumentException.class, () -> {
            teacherService.changePassword(invalidTeacher);
        });
    }

    @Test
    void changePassword_shouldThrowIllegalArgumentException_whenTeacherIsNotSavedInDb() {
        doReturn(Optional.empty()).when(teacherDao).findById(teacher.getId());

        assertThrows(IllegalArgumentException.class, () -> {
            teacherService.changePassword(teacher);
        });
    }

    @Test
    void changePassword_shouldChangePassword_whenTeacherIsValidAndExistsInDb() {
        doReturn(Optional.of(teacher)).when(teacherDao).findById(teacher.getId());

        teacher.setPassword("987654321");

        ArgumentCaptor<Teacher> captor = ArgumentCaptor.forClass(Teacher.class);

        teacherService.changePassword(teacher);

        verify(teacherDao).save(captor.capture());

        Teacher savedTeacher = captor.getValue();
        assertEquals(teacher.getId(), savedTeacher.getId());
        assertEquals(teacher.getName(), savedTeacher.getName());
        assertEquals(teacher.getSurname(), savedTeacher.getSurname());
        assertEquals(teacher.getEmail(), savedTeacher.getEmail());

        assertEquals(teacher.getPassword(), savedTeacher.getPassword());
    }

    @Test
    void delete_shouldThrowIllegalArgumentException_when_teacherIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            teacherService.delete(null);
        });
    }

    @Test
    void delete_shouldThrowIllegalArgumentException_when_teacherIsInvalid() {
        Teacher invalidTeacher = new Teacher();
        assertThrows(IllegalArgumentException.class, () -> {
            teacherService.delete(invalidTeacher);
        });
    }

    @Test
    void delete_shouldThrowIllegalArgumentException_when_teacherIsNotSavedInDb() {
        doReturn(false).when(teacherDao).existsById(teacher.getId());
        assertThrows(IllegalArgumentException.class, () -> {
            teacherService.delete(teacher);
        });
    }

    @Test
    void delete_shouldDeleteTeacher_when_teacherIsValidAndExistsInDb() {
        doReturn(true).when(teacherDao).existsById(teacher.getId());

        teacherService.delete(teacher);

        verify(teacherDao).delete(teacher);
    }

}
