package ua.foxminded.mykyta.zemlianyi.university.service;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ua.foxminded.mykyta.zemlianyi.university.Constants;
import ua.foxminded.mykyta.zemlianyi.university.dao.RoomDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Room;

@Service
public class RoomServiceImpl implements RoomService {
    private static Logger logger = LogManager.getLogger(RoomServiceImpl.class.getName());
    private RoomDao roomDao;

    public RoomServiceImpl(RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    @Override
    public Room addNew(Room room) {
        ObjectChecker.checkNullAndVerify(room);
        if (roomDao.existsByNumber(room.getNumber())) {
            throw new IllegalArgumentException(room.getNumber() + Constants.ROOM_ADD_NEW_ERROR_EXISTS_BY_NUMBER);
        }
        logger.info("Adding new room - {}", room);
        return roomDao.save(room);
    }

    @Override
    public Room update(Room room) {
        ObjectChecker.checkNullAndVerify(room);
        Room mergedRoom = mergeWithExisting(room);
        logger.info("Updating room - {}", mergedRoom);
        return roomDao.save(mergedRoom);
    }

    private Room mergeWithExisting(Room newRoom) {
        ObjectChecker.checkNull(newRoom);
        Optional<Room> existingRoomOpt = roomDao.findById(newRoom.getId());

        if (existingRoomOpt.isPresent()) {
            Room existingRoom = existingRoomOpt.get();
            if (!(existingRoom.getNumber() == newRoom.getNumber())) {
                if (roomDao.existsByNumber(newRoom.getNumber())) {
                    throw new IllegalArgumentException(
                            newRoom.getNumber() + Constants.ROOM_ADD_NEW_ERROR_EXISTS_BY_NUMBER);
                } else {
                    existingRoom.setNumber(newRoom.getNumber());
                }
            }

            return existingRoom;

        } else {
            throw new IllegalArgumentException(Constants.OBJECT_UPDATE_FAIL_DOES_NOT_EXIST);
        }
    }

    @Override
    public void delete(Room room) {
        ObjectChecker.checkNullAndVerify(room);
        ObjectChecker.checkIfExistsInDb(room, roomDao);
        room.clearLectures();
        logger.info("Updating room - {}", room);
        roomDao.delete(room);
    }

    @Override
    public Page<Room> findAll(Pageable pageable) {
        return roomDao.findAll(pageable);
    }

    @Override
    public Optional<Room> findById(Long id) {
        return roomDao.findById(id);
    }

}
