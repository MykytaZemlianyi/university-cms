package ua.foxminded.mykyta.zemlianyi.university.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ua.foxminded.mykyta.zemlianyi.university.Constants;
import ua.foxminded.mykyta.zemlianyi.university.dao.RoomDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Room;

public class RoomServiceImpl implements RoomService {
    private static Logger logger = LogManager.getLogger(RoomServiceImpl.class.getName());
    private RoomDao roomDao;

    public RoomServiceImpl(RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    @Override
    public Room addNew(Room room) {
        if (room == null || !room.verify()) {
            throw new IllegalArgumentException(Constants.ROOM_OBJECT_INVALID_MSG);
        }
        logger.info("Adding new room - {}", room);
        return roomDao.save(room);
    }

    @Override
    public Room update(Room room) {
        if (room == null || !room.verify()) {
            throw new IllegalArgumentException(Constants.ROOM_OBJECT_INVALID_MSG);
        }
        if (roomDao.existsById(room.getId())) {
            logger.info("Updating room - {}", room);
            return roomDao.save(room);
        } else {
            throw new IllegalArgumentException(Constants.ROOM_UPDATE_FAIL_DOES_NOT_EXIST);
        }
    }

}
