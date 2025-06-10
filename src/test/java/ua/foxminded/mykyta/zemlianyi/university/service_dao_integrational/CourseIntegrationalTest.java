package ua.foxminded.mykyta.zemlianyi.university.service_dao_integrational;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import jakarta.persistence.EntityManager;
import ua.foxminded.mykyta.zemlianyi.university.dao.CourseDao;
import ua.foxminded.mykyta.zemlianyi.university.dao.GroupDao;
import ua.foxminded.mykyta.zemlianyi.university.dao.TeacherDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Course;
import ua.foxminded.mykyta.zemlianyi.university.dto.Group;
import ua.foxminded.mykyta.zemlianyi.university.dto.Teacher;
import ua.foxminded.mykyta.zemlianyi.university.service.CourseServiceImpl;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = { "/sql/clear_tables.sql",
        "/sql/sample_data.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class CourseIntegrationalTest {

    @Autowired
    EntityManager em;

    @Autowired
    CourseServiceImpl service;
    @Autowired
    CourseDao dao;

    @Autowired
    TeacherDao teacherDao;
    @Autowired
    GroupDao groupDao;

    Course course1 = new Course();
    Course course2 = new Course();

    Teacher teacher1 = new Teacher();
    Teacher teacher2 = new Teacher();

    Group group1 = new Group();

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

        teacher2.setId(2L);
        teacher2.setName("Pawel");
        teacher2.setSurname("Prysak");
        teacher2.setEmail("pprysak@gmail.com");
        teacher2.setPassword("12345");

        group1.setId(1L);
        group1.setName("AA-11");

    }

    @Test
    void addNew_shouldSaveCourseToDb_whenAllFieldsAreValid() {
        Course newCourse = new Course();
        newCourse.setName("Math");
        newCourse.setTeacher(teacher1);

        service.addNew(newCourse);

        Course savedCourse = service.findById(newCourse.getId()).get();

        assertEquals(newCourse, savedCourse);
        assertTrue(teacherContainsCourse(teacher1, newCourse));

    }

    @Test
    void update_shouldRemoveTeacher_whenTeacherIsNullAfterUpdate() {
        course1.setTeacher(null);
        service.update(course1);
        assertFalse(teacherContainsCourse(teacher1, course1));

        Course savedCourse = service.findById(course1.getId()).get();
        assertNull(savedCourse.getTeacher());
    }

    @Test
    void update_shouldChangeTeacher_whenAnotherTeacherSelected() {
        course1.setTeacher(teacher2);

        service.update(course1);

        assertFalse(teacherContainsCourse(teacher1, course1));
        assertTrue(teacherContainsCourse(teacher2, course1));

        Course savedCourse = service.findById(course1.getId()).get();
        assertEquals(teacher2, savedCourse.getTeacher());
    }

    @Test
    void delete_shouldNotDeleteAssignedTeacher_whenCourseDeleted() {
        service.delete(course1);
        assertTrue(teacherDao.existsById(teacher1.getId()));
    }

    @Test
    void delete_shouldNotDeleteAssignedGroups_whenCourseDeleted() {
        service.delete(course1);
        assertTrue(groupDao.existsById(group1.getId()));
    }

    private boolean teacherContainsCourse(Teacher teacher, Course course) {
        return em.createQuery("SELECT c FROM Course c WHERE c.teacher = :teacher", Course.class)
                .setParameter("teacher", teacher).getResultList().contains(course);
    }

}
