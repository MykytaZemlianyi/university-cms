package ua.foxminded.mykyta.zemlianyi.university.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import ua.foxminded.mykyta.zemlianyi.university.dao.RoomDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Room;
import ua.foxminded.mykyta.zemlianyi.university.exceptions.RoomDuplicateException;
import ua.foxminded.mykyta.zemlianyi.university.exceptions.RoomNotFoundException;

@SpringBootTest(classes = { RoomServiceImpl.class })
class RoomServiceImplTest {

    @MockitoBean
    RoomDao roomDao;

    @MockitoBean
    PasswordEncoder encoder;

    @Autowired
    RoomServiceImpl roomService;

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

        assertThrows(RoomDuplicateException.class, () -> {
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

        assertThrows(RoomNotFoundException.class, () -> {
            roomService.update(untrackedRoom);
        });
    }

    @Test
    void update_shouldUpdateRoom_whenRoomValidAndSavedBeforeUpdate() {

        Room managedRoom = new Room();
        managedRoom.setId(1L);
        managedRoom.setNumber(0);

        Room updatedRoom = new Room();
        updatedRoom.setId(1L);
        updatedRoom.setNumber(1);

        when(roomDao.findById(1L)).thenReturn(Optional.of(managedRoom));

        roomService.update(updatedRoom);

        verify(roomDao).save(updatedRoom);
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

        verify(roomDao).deleteById(room.getId());
    }

}
