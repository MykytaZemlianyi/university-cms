package ua.foxminded.mykyta.zemlianyi.university.service_dao_integrational;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import ua.foxminded.mykyta.zemlianyi.university.dao.CourseDao;
import ua.foxminded.mykyta.zemlianyi.university.dao.GroupDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Course;
import ua.foxminded.mykyta.zemlianyi.university.dto.Group;
import ua.foxminded.mykyta.zemlianyi.university.dto.Student;
import ua.foxminded.mykyta.zemlianyi.university.service.GroupServiceImpl;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = { "/sql/clear_tables.sql",
        "/sql/sample_data.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class GroupIntegrationalTest {
    @Autowired
    EntityManager em;

    @Autowired
    GroupServiceImpl service;

    @Autowired
    GroupDao dao;

    @Autowired
    CourseDao courseDao;

    Group group1 = new Group();

    Student student1 = new Student();

    Student studentWithoutGroup = new Student();
    Course courseWithoutGroup = new Course();

    @BeforeEach
    void setUp() {
        student1.setId(1L);
        student1.setName("Mykyta");
        student1.setSurname("Zemlianyi");
        student1.setEmail("mzemlianyi@gmail.com");
        student1.setPassword("mz2004");

        group1.setId(1L);
        group1.setName("AA-11");
        group1.addStudent(student1);

        courseWithoutGroup.setId(2L);
        courseWithoutGroup.setName("Computer Science 2");

        studentWithoutGroup.setId(2L);
        studentWithoutGroup.setName("Maksym");
        studentWithoutGroup.setSurname("Maksymov");
        studentWithoutGroup.setEmail("mmaksymov@gmail.com");
        studentWithoutGroup.setPassword("12345");

    }

    @Test
    void addNew_shouldSaveGroupAndAllRelations_whenFieldsAreValid() {
        Set<Student> newStudents = new HashSet<>();
        newStudents.add(studentWithoutGroup);

        Set<Course> newCourses = new HashSet<>();
        newCourses.add(courseWithoutGroup);

        Group newGroup = new Group();
        newGroup.setName("CC-33");
        newGroup.setStudents(newStudents);
        newGroup.setCourses(newCourses);

        service.addNew(newGroup);

        Group savedGroup = service.findById(newGroup.getId()).get();

        assertEquals(newGroup, savedGroup);

        assertTrue(studentHasGroupAfterUpdate(studentWithoutGroup, newGroup));

        List<Course> coursesWithGroupAssigned = courseDao.findByGroups(newGroup);
        assertArrayEquals(newCourses.toArray(), coursesWithGroupAssigned.toArray());
    }

    private boolean studentHasGroupAfterUpdate(Student student, Group group) {
        try {
            return em.createQuery("SELECT s.group FROM Student s WHERE s.id = :studentId", Group.class)
                    .setParameter("studentId", student.getId()).getSingleResult().equals(group);

        } catch (NoResultException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

}
