package ua.foxminded.mykyta.zemlianyi.university.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import ua.foxminded.mykyta.zemlianyi.university.dao.RoomDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Room;

@SpringBootTest
class RoomServiceImplTest {
    @MockitoBean
    RoomDao roomDao;

    @Autowired
    RoomService roomService;

    @Test
    void addNew_shouldThrowIllegalArgumentException_whenRoomIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            roomService.addNew(null);
        });
    }

    @Test
    void addNew_shouldThrowIllegalArgumentException_whenRoomIsInvalid() {
        Room invalidRoom = new Room();

        assertThrows(IllegalArgumentException.class, () -> {
            roomService.addNew(invalidRoom);
        });
    }

    @Test
    void addNew_shouldThrowIllegalArgumentException_whenRoomWithSameNumberExists() {
        Room roomWithSameNumber = new Room();
        roomWithSameNumber.setNumber(1);

        doReturn(true).when(roomDao).existsByNumber(roomWithSameNumber.getNumber());

        assertThrows(IllegalArgumentException.class, () -> {
            roomService.addNew(roomWithSameNumber);
        });
    }

    @Test
    void addNew_shouldSaveRoom_whenRoomIsValid() {
        Room room = new Room();
        room.setNumber(1);

        roomService.addNew(room);

        verify(roomDao).save(room);
    }

    @Test
    void update_shouldThrowIllegalArgumentException_whenRoomIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            roomService.update(null);
        });
    }

    @Test
    void update_shouldThrowIllegalArgumentException_whenRoomIsInvalid() {
        Room invalidRoom = new Room();

        assertThrows(IllegalArgumentException.class, () -> {
            roomService.update(invalidRoom);
        });
    }

    @Test
    void update_shouldThrowIllegalArgumentException_whenRoomDidNotSavedBeforeUpdate() {
        Room untrackedRoom = new Room();
        untrackedRoom.setId(1L);
        untrackedRoom.setNumber(1);

        when(roomDao.existsById(1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            roomService.update(untrackedRoom);
        });
    }

    @Test
    void update_shouldUpdateRoom_whenRoomValidAndSavedBeforeUpdate() {
        Room trackedRoom = new Room();
        trackedRoom.setId(1L);
        trackedRoom.setNumber(1);

        when(roomDao.existsById(1L)).thenReturn(true);

        roomService.update(trackedRoom);

        verify(roomDao).save(trackedRoom);
    }

    @Test
    void delete_shouldThrowIllegalArgumentException_when_roomIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            roomService.delete(null);
        });
    }

    @Test
    void delete_shouldThrowIllegalArgumentException_when_roomIsInvalid() {
        Room invalidRoom = new Room();
        assertThrows(IllegalArgumentException.class, () -> {
            roomService.delete(invalidRoom);
        });
    }

    @Test
    void delete_shouldThrowIllegalArgumentException_when_roomIsNotSavedInDb() {
        Room room = new Room();
        room.setNumber(1);

        doReturn(false).when(roomDao).existsById(room.getId());
        assertThrows(IllegalArgumentException.class, () -> {
            roomService.delete(room);
        });
    }

    @Test
    void delete_shouldDeleteRoom_when_roomIsValidAndExistsInDb() {
        Room room = new Room();
        room.setNumber(1);

        doReturn(true).when(roomDao).existsById(room.getId());

        roomService.delete(room);

        verify(roomDao).delete(room);
    }

}
