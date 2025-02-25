package ua.foxminded.mykyta.zemlianyi.university.service;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import ua.foxminded.mykyta.zemlianyi.university.dao.CourseDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Course;
import ua.foxminded.mykyta.zemlianyi.university.dto.Group;
import ua.foxminded.mykyta.zemlianyi.university.dto.Student;
import ua.foxminded.mykyta.zemlianyi.university.dto.Teacher;

@SpringBootTest(classes = { CourseServiceImpl.class })
class CourseServiceImplTest {
    @MockitoBean
    CourseDao courseDao;

    @Autowired
    CourseService courseService;

    @Test
    void addNew_shouldThrowIllegalArumentException_whenCourseIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            courseService.addNew(null);
        });
    }

    @Test
    void addNew_shouldThrowIllegalArumentException_whenCourseIsInvalid() {
        Course invalidCourse = new Course();

        assertThrows(IllegalArgumentException.class, () -> {
            courseService.addNew(invalidCourse);
        });
    }

    @Test
    void addNew_shouldThrowIllegalArgumentException_whenAddingSameCourse() {
        Course courseWithSameName = new Course();
        courseWithSameName.setName("Java Programming");

        doReturn(true).when(courseDao).existsByName(courseWithSameName.getName());

        assertThrows(IllegalArgumentException.class, () -> {
            courseService.addNew(courseWithSameName);
        });
    }

    @Test
    void addNew_shouldSaveCourse_whenCourseIsValid() {
        Course course = new Course();
        course.setName("Valid Course");
        courseService.addNew(course);
        verify(courseDao).save(course);
    }

    @Test
    void update_shouldThrowIllegalArumentException_whenCourseIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            courseService.update(null);
        });
    }

    @Test
    void update_shouldThrowIllegalArumentException_whenCourseIsInvalid() {
        Course invalidCourse = new Course();

        assertThrows(IllegalArgumentException.class, () -> {
            courseService.update(invalidCourse);
        });
    }

    @Test
    void update_shouldThrowIllegalArgumentException_whenCourseDidNotSavedBeforeUpdate() {
        Course untrackedCourse = new Course();
        untrackedCourse.setId(1L);
        untrackedCourse.setName("Course");

        when(courseDao.existsById(1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            courseService.update(untrackedCourse);
        });
    }

    @Test
    void update_shouldUpdateCourse_whenCourseValidAndSavedBeforeUpdate() {
        Course trackedCourse = new Course();
        trackedCourse.setId(1L);
        trackedCourse.setName("Course");

        when(courseDao.existsById(1L)).thenReturn(true);

        courseService.update(trackedCourse);

        verify(courseDao).save(trackedCourse);
    }

    @Test
    void findForTeacher_shouldThrowIllegalArgumentException_whenTeacherIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            courseService.findForTeacher(null);
        });
    }

    @Test
    void findForTeacher_shouldThrowIllegalArgumentException_whenTeacherIdIsNull() {
        Teacher teacherWithoutId = new Teacher();
        assertThrows(IllegalArgumentException.class, () -> {
            courseService.findForTeacher(teacherWithoutId);
        });
    }

    @Test
    void findForTeacher_shouldReturnCoursesForTeacher_whenTeacherIsCorrect() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setName("Marek");
        teacher.setSurname("Szepski");
        teacher.setEmail("mszepski@gmail.com");
        teacher.setPassword("123456789");
        courseService.findForTeacher(teacher);
        verify(courseDao).findByTeacher(teacher);
    }

    @Test
    void findForStudent_shouldThrowIllegalArgumentException_whenStudentIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            courseService.findForStduent(null);
        });
    }

    @Test
    void findForStudent_shouldThrowIllegalArgumentException_whenStudentIdIsNull() {
        Student studentWithoutId = new Student();
        assertThrows(IllegalArgumentException.class, () -> {
            courseService.findForStduent(studentWithoutId);
        });
    }

    @Test
    void findForStudent_shouldThrowIllegalArgumentException_whenStudentDoesNotHaveGroup() {
        Student studentWithoutGroup = new Student();
        studentWithoutGroup.setId(1L);
        assertThrows(IllegalArgumentException.class, () -> {
            courseService.findForStduent(studentWithoutGroup);
        });
    }

    @Test
    void findForStudent_shouldReturnCourses_whenStudendIsCorrectAndHaveGroup() {
        Course course = new Course();
        course.setId(1L);
        course.setName("Computer Science");

        Group group = new Group();
        group.addCourse(course);

        Student student = new Student();
        student.setId(1L);
        student.setGroup(group);

        List<Course> returnedCourses = new ArrayList<>();
        returnedCourses.add(course);

        doReturn(returnedCourses).when(courseDao).findByGroups(group);

        List<Course> expectedCourses = new ArrayList<>();
        expectedCourses.add(course);

        List<Course> actualCourses = courseService.findForStduent(student);

        verify(courseDao).findByGroups(group);

        assertArrayEquals(expectedCourses.toArray(), actualCourses.toArray());
    }

    @Test
    void delete_shouldThrowIllegalArgumentException_when_courseIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            courseService.delete(null);
        });
    }

    @Test
    void delete_shouldThrowIllegalArgumentException_when_courseIsInvalid() {
        Course invalidCourse = new Course();
        assertThrows(IllegalArgumentException.class, () -> {
            courseService.delete(invalidCourse);
        });
    }

    @Test
    void delete_shouldThrowIllegalArgumentException_when_courseIsNotSavedInDb() {
        Course course = new Course();
        course.setName("Valid Course");

        doReturn(false).when(courseDao).existsById(course.getId());
        assertThrows(IllegalArgumentException.class, () -> {
            courseService.delete(course);
        });
    }

    @Test
    void delete_shouldDeleteCourse_when_courseIsValidAndExistsInDb() {
        Course course = new Course();
        course.setName("Valid Course");

        doReturn(true).when(courseDao).existsById(course.getId());

        courseService.delete(course);

        verify(courseDao).delete(course);
    }
}
