package ua.foxminded.mykyta.zemlianyi.university.service_dao_integrational;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import ua.foxminded.mykyta.zemlianyi.university.dao.LectureDao;
import ua.foxminded.mykyta.zemlianyi.university.dao.RoomDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Lecture;
import ua.foxminded.mykyta.zemlianyi.university.dto.Room;
import ua.foxminded.mykyta.zemlianyi.university.service.RoomServiceImpl;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = { "/sql/clear_tables.sql",
        "/sql/sample_data.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class RoomIntegrationalTest {

    @Autowired
    LectureDao lectureDao;

    @Autowired
    RoomDao dao;

    @Autowired
    RoomServiceImpl service;

    Room room = new Room();
    Lecture lecture = new Lecture();

    @BeforeEach
    void setUp() {
        room.setId(1L);
        room.setNumber(100);

        lecture = lectureDao.findById(4L).get();
        room.addLecture(lecture);
    }

    @Test
    void delete_shouldNotDeledAssociatedEntitiesFromDb() {
        service.deleteById(room);

        assertFalse(dao.existsById(room.getId()));
        assertTrue(lectureDao.existsById(lecture.getId()));
    }

}
