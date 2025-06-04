package ua.foxminded.mykyta.zemlianyi.university.service_dao_integrational;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

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
import ua.foxminded.mykyta.zemlianyi.university.dao.LectureDao;
import ua.foxminded.mykyta.zemlianyi.university.dao.RoomDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Course;
import ua.foxminded.mykyta.zemlianyi.university.dto.Lecture;
import ua.foxminded.mykyta.zemlianyi.university.dto.LectureType;
import ua.foxminded.mykyta.zemlianyi.university.dto.Room;
import ua.foxminded.mykyta.zemlianyi.university.service.LectureServiceImpl;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = { "/sql/clear_tables.sql",
        "/sql/sample_data.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class LectureIntegrationalTest {
    @Autowired
    EntityManager em;

    @Autowired
    LectureDao dao;

    @Autowired
    RoomDao roomDao;

    @Autowired
    CourseDao courseDao;

    @Autowired
    LectureServiceImpl service;

    Lecture lecture = new Lecture();
    Room room = new Room();
    Course course = new Course();

    @BeforeEach
    void setUp() {
        room.setId(1L);
        room.setNumber(100);

        course.setId(1L);
        course.setName("Computer Science");

    }

    @Test
    void addNew_shouldSaveLectureRelationsWhenSavedInDb() {
        Lecture newLecture = new Lecture();
        newLecture.setLectureType(LectureType.LABORATORIUM);
        newLecture.setTimeStart(LocalDateTime.of(2025, 04, 15, 10, 00));
        newLecture.setTimeEnd(LocalDateTime.of(2025, 04, 15, 12, 00));
        newLecture.setRoom(room);
        newLecture.setCourse(course);

        service.addNew(newLecture);

        assertTrue(roomHasLecture(room, newLecture));
        assertTrue(courseHasLecture(course, newLecture));
    }

    @Test
    void update_shouldChangeAssociatedRelations_whenRelationsChangedDuringUpdate() {
        Course anotherCourse = new Course();
        anotherCourse.setId(2L);
        anotherCourse.setName("Computer Science 2");
        Room anotherRoom = new Room();

        anotherRoom.setId(2L);
        anotherRoom.setNumber(101);

        Lecture lectureWithCourseAndRoom = dao.findById(4L).get();
        lectureWithCourseAndRoom.setCourse(anotherCourse);
        lectureWithCourseAndRoom.setRoom(anotherRoom);

        service.update(lectureWithCourseAndRoom);

        assertFalse(roomHasLecture(room, lectureWithCourseAndRoom));
        assertFalse(courseHasLecture(course, lectureWithCourseAndRoom));

        assertTrue(roomHasLecture(anotherRoom, lectureWithCourseAndRoom));
        assertTrue(courseHasLecture(anotherCourse, lectureWithCourseAndRoom));
    }

    @Test
    void delete_shouldNotDeleteAssociatedRelations_whenLectureDeleted() {
        Lecture lectureWithCourseAndRoom = dao.findById(4L).get();
        service.deleteById(lectureWithCourseAndRoom);

        assertFalse(dao.existsById(lectureWithCourseAndRoom.getId()));
        assertTrue(roomDao.existsById(room.getId()));
        assertTrue(courseDao.existsById(course.getId()));
    }

    private boolean roomHasLecture(Room room, Lecture lecture) {
        return em.createQuery("SELECT lec FROM Lecture lec WHERE lec.room = :room").setParameter("room", room)
                .getResultList().contains(lecture);
    }

    private boolean courseHasLecture(Course course, Lecture lecture) {
        return em.createQuery("SELECT lec FROM Lecture lec WHERE lec.course = :course").setParameter("course", course)
                .getResultList().contains(lecture);
    }

}
