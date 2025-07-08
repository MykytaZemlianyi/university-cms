package ua.foxminded.mykyta.zemlianyi.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import ua.foxminded.mykyta.zemlianyi.university.dao.TeacherDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Teacher;
import ua.foxminded.mykyta.zemlianyi.university.exceptions.TeacherDuplicateException;
import ua.foxminded.mykyta.zemlianyi.university.exceptions.TeacherNotFoundException;

@SpringBootTest(classes = { TeacherServiceImpl.class })
class TeacherServiceImplTest {

    @MockitoBean
    TeacherDao teacherDao;

    @MockitoBean
    PasswordEncoder encoder;

    @Autowired
    TeacherServiceImpl teacherService;

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

        assertThrows(TeacherDuplicateException.class, () -> {
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
        when(teacherDao.findById(teacher.getId())).thenReturn(Optional.empty());
        assertThrows(TeacherNotFoundException.class, () -> {
            teacherService.update(teacher);
        });
    }

    @Test
    void update_shouldUpdate_whenTeacherIsCorrectAndExistsInDb() {
        doReturn(Optional.of(teacher)).when(teacherDao).findById(teacher.getId());
        doReturn("encryptedPassword").when(encoder).encode(teacher.getPassword());
        teacherService.update(teacher);

        verify(teacherDao).save(teacher);
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

        verify(teacherDao).deleteById(teacher.getId());
    }

}
