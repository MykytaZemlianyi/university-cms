package ua.foxminded.mykyta.zemlianyi.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import ua.foxminded.mykyta.zemlianyi.university.dto.Group;
import ua.foxminded.mykyta.zemlianyi.university.dto.Student;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(GroupDaoTest.TestContainersConfig.class)
@Sql(scripts = { "/sql/clear_tables.sql",
        "/sql/sample_data.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class GroupDaoTest {
    @Autowired
    GroupDao groupDao;
    Student student = new Student();
    Group group1 = new Group();

    @TestConfiguration
    static class TestContainersConfig {
        @Container
        @ServiceConnection
        static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");
    }

    @BeforeEach
    void setUp() {
        student.setId(1L);
        student.setName("Mykyta");
        student.setSurname("Zemlianyi");
        student.setEmail("mzemlianyi@gmail.com");
        student.setPassword("mz2004");

        group1.setId(1L);
        group1.setName("AA-11");
        group1.addStudent(student);
    }

    @Test
    void findByStudent_shouldReturnCorrectGroup_whenStudentAssignedToThisGroup() {
        Group expectedGroup = group1;

        Group actualGroup = groupDao.findByStudents(student);

        assertEquals(expectedGroup, actualGroup);
    }

}
