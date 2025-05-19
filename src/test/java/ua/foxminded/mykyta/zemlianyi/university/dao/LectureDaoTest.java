package ua.foxminded.mykyta.zemlianyi.university.dao;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
import ua.foxminded.mykyta.zemlianyi.university.dto.Lecture;
import ua.foxminded.mykyta.zemlianyi.university.dto.LectureType;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(LectureDaoTest.TestContainersConfig.class)
@Sql(scripts = { "/sql/clear_tables.sql",
        "/sql/sample_data.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class LectureDaoTest {
    @Autowired
    LectureDao lectureDao;

    Course course1 = new Course();
    Course course2 = new Course();
    Lecture lecture = new Lecture();

    @TestConfiguration
    static class TestContainersConfig {
        @Container
        @ServiceConnection
        static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");
    }

    @BeforeEach
    void setUp() {
        course1.setId(1L);
        course1.setName("Computer Science");

        course2.setId(2L);
        course2.setName("Computer Science 2");

        lecture.setId(4L);
        lecture.setLectureType(LectureType.SEMINAR);
        lecture.setCourse(course1);
        lecture.setTimeStart(LocalDateTime.of(2025, 1, 17, 14, 00));
        lecture.setTimeEnd(LocalDateTime.of(2025, 1, 17, 15, 30));

        course1.addLecture(lecture);
    }

    @Test
    void findByCourse_shouldReturnCorrectCourse_whenCourseHaveLecture() {
        List<Lecture> expLectures = new ArrayList<Lecture>();
        expLectures.add(lecture);

        List<Lecture> actLectures = lectureDao.findByCourse(course1);

        assertArrayEquals(expLectures.toArray(), actLectures.toArray());
    }

    @Test
    void findByCourseAndDateBetween_shouldFilterLectures_whenDateIsOneDay() {

        Lecture lectureDayTwo = new Lecture();
        lectureDayTwo.setId(2L);
        lectureDayTwo.setLectureType(LectureType.LECTURE);
        lectureDayTwo.setCourse(course2);
        lectureDayTwo.setTimeStart(LocalDateTime.of(2025, 1, 16, 11, 0));
        lectureDayTwo.setTimeEnd(LocalDateTime.of(2025, 1, 16, 12, 30));

        List<Lecture> expLectures = new ArrayList<>();
        expLectures.add(lectureDayTwo);

        List<Lecture> actualLectures = lectureDao.findByCourseAndTimeStartBetween(course2,
                LocalDateTime.of(2025, 1, 16, 0, 0), LocalDateTime.of(2025, 1, 16, 23, 59, 59));

        assertArrayEquals(expLectures.toArray(), actualLectures.toArray());
    }

}
