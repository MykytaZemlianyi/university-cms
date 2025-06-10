package ua.foxminded.mykyta.zemlianyi.university.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import ua.foxminded.mykyta.zemlianyi.university.dto.Group;
import ua.foxminded.mykyta.zemlianyi.university.dto.Student;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@ComponentScan(basePackages = "ua.foxminded.mykyta.zemlianyi.university")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = { "/sql/clear_tables.sql",
        "/sql/sample_data.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class GroupDaoTest {
    @Autowired
    GroupDao groupDao;
    Student student = new Student();
    Group group1 = new Group();

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
