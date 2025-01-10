package ua.foxminded.mykyta.zemlianyi.university.dao;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import ua.foxminded.mykyta.zemlianyi.university.UniversityApplication;
import ua.foxminded.mykyta.zemlianyi.university.dto.Course;
import ua.foxminded.mykyta.zemlianyi.university.dto.Teacher;

@Testcontainers
@SpringBootTest(classes = { UniversityApplication.class })
@ActiveProfiles("test")
@ComponentScan(basePackages = "ua.foxminded.mykyta.zemlianyi.university")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = { "/sql/clear_tables.sql",
        "/sql/sample_data.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class CourseDaoTest {
    @Autowired
    CourseDao courseDao;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    @Test
    void findCourseByTeacher_shouldReturnCourses_whenInputCorrectTeacher() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        Course expectedCourse = new Course();
        expectedCourse.setId(1L);
        expectedCourse.setName("Computer Science");
        expectedCourse.setTeacher(teacher);

        List<Course> courses = courseDao.findCoursesByTeacher(1L);
        assertFalse(courses.isEmpty());
    }

}
