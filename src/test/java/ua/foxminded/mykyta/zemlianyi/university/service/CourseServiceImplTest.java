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

@SpringBootTest(classes = { CourseServiceImpl.class })
class CourseServiceImplTest {
    @MockitoBean
    CourseDao courseDao;

    @Autowired
    CourseService courseService;

    @BeforeEach
    void setUp() throws Exception {

    }

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
}
