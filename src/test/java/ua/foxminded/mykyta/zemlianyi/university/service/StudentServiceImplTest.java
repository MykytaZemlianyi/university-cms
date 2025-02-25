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

import ua.foxminded.mykyta.zemlianyi.university.dao.StudentDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Student;

@SpringBootTest(classes = { StudentServiceImpl.class })
class StudentServiceImplTest {
    @MockitoBean
    StudentDao studentDao;

    @Autowired
    StudentService studentService;

    Student student = new Student();

    @BeforeEach
    void setUp() {
        student.setId(1L);
        student.setName("Mykyta");
        student.setSurname("Zemlianyi");
        student.setEmail("mzeml@gmail.com");
        student.setPassword("123456789");
    }

    @Test
    void addNew_shouldThrowIllegalArgumentException_whenStudentIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            studentService.addNew(null);
        });
    }

    @Test
    void addNew_shouldThrowIllegalArgumentException_whenStudentIsNotVerified() {
        Student invalidStudent = new Student();
        assertThrows(IllegalArgumentException.class, () -> {
            studentService.addNew(invalidStudent);
        });
    }

    @Test
    void addNew_shouldThrowIllegalArgumentException_whenStudentWithSameEmailExists() {
        Student studentWithSameEmail = new Student();
        studentWithSameEmail.setId(2L);
        studentWithSameEmail.setName("Matvii");
        studentWithSameEmail.setSurname("Zemlianin");
        studentWithSameEmail.setEmail(student.getEmail());
        studentWithSameEmail.setPassword("password123");
        doReturn(true).when(studentDao).existsByEmail(studentWithSameEmail.getEmail());

        assertThrows(IllegalArgumentException.class, () -> {
            studentService.addNew(studentWithSameEmail);
        });
    }

    @Test
    void addNew_shouldSaveStudent_whenStudentIsVerified() {

        studentService.addNew(student);

        verify(studentDao).save(student);
    }

    @Test
    void update_shouldThrowIllegalArgumentException_whenStudentIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            studentService.update(null);
        });
    }

    @Test
    void update_shouldThrowIllegalArgumentException_whenStudentIsNotVerified() {
        Student invalidStudent = new Student();
        assertThrows(IllegalArgumentException.class, () -> {
            studentService.update(invalidStudent);
        });
    }

    @Test
    void update_shouldThrowIllegalArgumentException_whenStudentDoesNotExistsInDb() {
        doReturn(false).when(studentDao).existsById(student.getId());
        assertThrows(IllegalArgumentException.class, () -> {
            studentService.update(student);
        });
    }

    @Test
    void update_shouldUpdate_whenStudentIsCorrectAndExistsInDb() {
        doReturn(true).when(studentDao).existsById(student.getId());

        studentService.update(student);

        verify(studentDao).save(student);
    }

    @Test
    void changePassword_shouldThrowIllegalArgumentException_whenStudentIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            studentService.changePassword(null);
        });
    }

    @Test
    void changePassword_shouldThrowIllegalArgumentException_whenStudentIsInvalid() {
        Student invalidStudent = new Student();
        assertThrows(IllegalArgumentException.class, () -> {
            studentService.changePassword(invalidStudent);
        });
    }

    @Test
    void changePassword_shouldThrowIllegalArgumentException_whenStudentIsNotSavedInDb() {
        doReturn(Optional.empty()).when(studentDao).findById(student.getId());

        assertThrows(IllegalArgumentException.class, () -> {
            studentService.changePassword(student);
        });
    }

    @Test
    void changePassword_shouldChangePassword_whenStudentIsValidAndExistsInDb() {
        doReturn(Optional.of(student)).when(studentDao).findById(student.getId());

        student.setId(student.getId());
        student.setPassword("987654321");

        ArgumentCaptor<Student> captor = ArgumentCaptor.forClass(Student.class);

        studentService.changePassword(student);

        verify(studentDao).save(captor.capture());

        Student savedStudent = captor.getValue();
        assertEquals(student.getId(), savedStudent.getId());
        assertEquals(student.getName(), savedStudent.getName());
        assertEquals(student.getSurname(), savedStudent.getSurname());
        assertEquals(student.getEmail(), savedStudent.getEmail());

        assertEquals(student.getPassword(), savedStudent.getPassword());
    }

    @Test
    void delete_shouldThrowIllegalArgumentException_when_studentIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            studentService.delete(null);
        });
    }

    @Test
    void delete_shouldThrowIllegalArgumentException_when_studentIsInvalid() {
        Student invalidStudent = new Student();
        assertThrows(IllegalArgumentException.class, () -> {
            studentService.delete(invalidStudent);
        });
    }

    @Test
    void delete_shouldThrowIllegalArgumentException_when_studentIsNotSavedInDb() {
        doReturn(false).when(studentDao).existsById(student.getId());
        assertThrows(IllegalArgumentException.class, () -> {
            studentService.delete(student);
        });
    }

    @Test
    void delete_shouldDeleteStudent_when_studentIsValidAndExistsInDb() {
        doReturn(true).when(studentDao).existsById(student.getId());

        studentService.delete(student);

        verify(studentDao).delete(student);
    }

}
