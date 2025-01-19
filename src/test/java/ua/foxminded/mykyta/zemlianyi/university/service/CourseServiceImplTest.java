package ua.foxminded.mykyta.zemlianyi.university.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
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
        Group group = new Group();
        Student student = new Student();
        student.setId(1L);
        student.setGroup(group);

        courseService.findForStduent(student);
        verify(courseDao).findByGroups(group);
    }
}
