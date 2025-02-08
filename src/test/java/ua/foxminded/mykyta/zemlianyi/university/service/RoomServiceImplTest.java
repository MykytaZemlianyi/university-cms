package ua.foxminded.mykyta.zemlianyi.university.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import ua.foxminded.mykyta.zemlianyi.university.dao.RoomDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Room;

@SpringBootTest(classes = { RoomServiceImpl.class })
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
}
