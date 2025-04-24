package ua.foxminded.mykyta.zemlianyi.university.dao;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;
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

import ua.foxminded.mykyta.zemlianyi.university.dto.Course;
import ua.foxminded.mykyta.zemlianyi.university.dto.Group;
import ua.foxminded.mykyta.zemlianyi.university.dto.Teacher;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(CourseDaoTest.TestContainersConfig.class)
@Sql(scripts = { "/sql/clear_tables.sql",
        "/sql/sample_data.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class CourseDaoTest {
    @Autowired
    CourseDao courseDao;

    Teacher teacher1;

    @TestConfiguration
    static class TestContainersConfig {
        @Container
        @ServiceConnection
        static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");
    }

    @BeforeEach
    void setUp() {
        teacher1 = new Teacher();
        teacher1.setId(1L);
        teacher1.setName("Marek");
        teacher1.setSurname("Szepski");
        teacher1.setEmail("mszepski@gmail.com");
        teacher1.setPassword("szepski99");
    }

    @Test
    void findByTeacher_shouldReturnCourses_whenInputCorrectTeacher() {
        Course expectedCourse = new Course();
        expectedCourse.setId(1L);
        expectedCourse.setName("Computer Science");
        expectedCourse.setTeacher(teacher1);

        List<Course> courses = courseDao.findByTeacher(teacher1);

        Course actualCourse = courses.get(0);

        assertEquals(expectedCourse, actualCourse);
    }

    @Test
    void findByGroups_shouldReturnCourses_whenInputAssociatedGroup() {
        Group group = new Group();
        group.setId(1L);
        group.setName("AA-11");

        Course expectedCourse = new Course();
        expectedCourse.setId(1L);
        expectedCourse.setName("Computer Science");
        List<Course> expectedCourseList = new ArrayList();
        expectedCourseList.add(expectedCourse);

        List<Course> actualCourseList = courseDao.findByGroups(group);

        assertArrayEquals(expectedCourseList.toArray(), actualCourseList.toArray());
    }

}
