package ua.foxminded.mykyta.zemlianyi.university.service_dao_integrational;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
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
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import ua.foxminded.mykyta.zemlianyi.university.dao.CourseDao;
import ua.foxminded.mykyta.zemlianyi.university.dao.TeacherDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Course;
import ua.foxminded.mykyta.zemlianyi.university.dto.Teacher;
import ua.foxminded.mykyta.zemlianyi.university.service.TeacherServiceImpl;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = { "/sql/clear_tables.sql",
        "/sql/sample_data.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class TeacherIntegrationalTest {
    @TestConfiguration
    static class TestContainersConfig {
        @Container
        @ServiceConnection
        static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17.0");
    }

    @Autowired
    TeacherServiceImpl service;

    @Autowired
    TeacherDao dao;

    @Autowired
    CourseDao courseDao;

    Teacher teacher1 = new Teacher();
    Teacher teacher2 = new Teacher();
    Course course1 = new Course();
    Course course2 = new Course();

    @BeforeEach
    void setUp() {
        course1.setId(1L);
        course1.setName("Computer Science");
        course2.setId(2L);
        course2.setName("Computer Science 2");

        teacher1.setId(1L);
        teacher1.setName("Marek");
        teacher1.setSurname("Szepski");
        teacher1.setEmail("mszepski@gmail.com");
        teacher1.setPassword("szepski99");
        teacher1.addCourse(course1);

        teacher2.setId(2L);
        teacher2.setName("Pawel");
        teacher2.setSurname("Prysak");
        teacher2.setEmail("pprysak@gmail.com");
        teacher2.setPassword("12345");
    }

    @Test
    void addNew_shouldSaveStudentInDb_whenAllFieldsAreValid() {
        Teacher newTeacher = new Teacher();
        newTeacher.setId(1L);
        newTeacher.setName("John");
        newTeacher.setSurname("Doe");
        newTeacher.setEmail("JohnDoe@gmail.com");
        newTeacher.setPassword("12345");
        newTeacher.addCourse(course1);

        service.addNew(newTeacher);

        Teacher savedStudent = dao.findByEmail(newTeacher.getEmail()).get();

        assertEquals(newTeacher, savedStudent);
    }

    @Test
    void update_shouldUpdateCourseFieldInDb_whenAddedNewCourseForTeacherDuringUpdate() {
        Set<Course> newCourses = new HashSet<>();
        newCourses.add(course1);
        newCourses.add(course2);

        teacher1.setCourses(newCourses);

        service.update(teacher1);

        Teacher updatedTeacher = dao.findById(teacher1.getId()).get();
        assertEquals(teacher1, updatedTeacher);

        Course updatedCourse1 = courseDao.findById(1L).get();
        Course updatedCourse2 = courseDao.findById(2L).get();

        assertEquals(updatedTeacher, updatedCourse1.getTeacher());
        assertEquals(updatedTeacher, updatedCourse2.getTeacher());
    }

    @Test
    void update_shouldUpdateCourseFieldInDb_whenCourseAddedToTeacherDuringUpdateAfterItWasNull() {
        Set<Course> newCourses = new HashSet<>();
        newCourses.add(course2);

        teacher2.setCourses(newCourses);

        service.update(teacher2);

        Teacher updatedTeacher = dao.findById(teacher2.getId()).get();
        assertEquals(teacher2, updatedTeacher);

        Course updatedCourse2 = courseDao.findById(2L).get();

        assertEquals(updatedTeacher, updatedCourse2.getTeacher());
    }

    @Test
    void update_shouldRemoveCourseFromTeacher_whenNewCoursesDoesNotContainOldCourseAfterUpdate() {
        Set<Course> newCourses = new HashSet<>();

        teacher1.setCourses(newCourses);

        service.update(teacher1);

        Teacher updatedTeacher = dao.findById(teacher1.getId()).get();
        assertEquals(teacher1, updatedTeacher);

        Course updatedCourse1 = courseDao.findById(1L).get();

        assertNull(updatedCourse1.getTeacher());
    }

    @Test
    void delete_shouldNotDeleteAssignedCourseFromDb_whenDeletingTeacher() {
        service.delete(teacher1);
        assertTrue(courseDao.existsById(course1.getId()));
    }

}
