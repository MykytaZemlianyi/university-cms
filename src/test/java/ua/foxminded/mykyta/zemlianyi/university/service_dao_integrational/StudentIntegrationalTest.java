package ua.foxminded.mykyta.zemlianyi.university.service_dao_integrational;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import jakarta.persistence.EntityManager;
import ua.foxminded.mykyta.zemlianyi.university.dao.GroupDao;
import ua.foxminded.mykyta.zemlianyi.university.dao.StudentDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Group;
import ua.foxminded.mykyta.zemlianyi.university.dto.Student;
import ua.foxminded.mykyta.zemlianyi.university.service.StudentServiceImpl;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = { "/sql/clear_tables.sql",
        "/sql/sample_data.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class StudentIntegrationalTest {
    @TestConfiguration
    static class TestContainersConfig {
        @Container
        @ServiceConnection
        static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17.0");
    }

    @Autowired
    EntityManager em;

    @Autowired
    StudentDao dao;

    @Autowired
    GroupDao groupDao;

    @Autowired
    StudentServiceImpl service;

    Group group1 = new Group();
    Group group2 = new Group();

    Student student1 = new Student();
    Student student2 = new Student();

    @BeforeEach
    void setUp() {
        group1.setId(1L);
        group1.setName("AA-11");

        group2.setId(2L);
        group2.setName("BB-22");

        student1.setId(1L);
        student1.setName("Mykyta");
        student1.setSurname("Zemlianyi");
        student1.setEmail("mzemlianyi@gmail.com");
        student1.setPassword("mz2004");
        student1.setGroup(group1);

        student2.setId(2L);
        student2.setName("Maksym");
        student2.setSurname("Maksymov");
        student2.setEmail("mmaksymov@gmail.com");
        student2.setPassword("12345");

    }

    @Test
    void addNew_shouldSaveStudentInDb_whenAllFieldsAreValid() {
        Student newStudent = new Student();
        newStudent.setName("John");
        newStudent.setSurname("Doe");
        newStudent.setEmail("johndoe@gmail.com");
        newStudent.setPassword("12345");
        newStudent.setGroup(group1);

        service.addNew(newStudent);

        Student savedStudent = dao.findByEmail(newStudent.getEmail()).get();

        assertEquals(newStudent, savedStudent);
    }

    @Test
    void update_shouldUpdateGroupFieldInDb_whenSelectedDifferentGroupForStudent() {
        student1.setGroup(group2);

        service.update(student1);

        Student updatedStudent = dao.findById(student1.getId()).get();
        assertEquals(student1, updatedStudent);

        assertFalse(getStudentsInGroup(group1).contains(student1));
        assertTrue(getStudentsInGroup(group2).contains(student1));
    }

    @Test
    void update_shouldUpdateGroupFieldInDb_whenGroupRemovedForStudent() {
        student1.setGroup(null);
        service.update(student1);
        Student updatedStudent = dao.findById(student1.getId()).get();
        assertEquals(student1, updatedStudent);
        assertFalse(getStudentsInGroup(group1).contains(student1));
    }

    @Test
    void update_shouldUpdateGroupFieldInDb_whenStudentAddedToTheGroup() {
        student2.setGroup(group1);
        service.update(student2);

        Student updatedStudent = dao.findById(student2.getId()).get();
        assertEquals(student2, updatedStudent);

        assertTrue(getStudentsInGroup(group1).contains(student1));
    }

    @Test
    void delete_shouldNotDeleteAssignedGroup_whenDeletingStudent() {
        service.delete(student1);
        assertTrue(groupDao.existsById(student1.getGroup().getId()));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    Set<Student> getStudentsInGroup(Group group) {
        List<Student> students = em.createQuery("SELECT s FROM Student s WHERE s.group = :group", Student.class)
                .setParameter("group", group).getResultList();

        return new HashSet<Student>(students);
    }

}
