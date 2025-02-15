package ua.foxminded.mykyta.zemlianyi.university.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
        ObjectChecker.check(room);
        logger.info("Adding new room - {}", room);
        return roomDao.save(room);
    }

    @Override
    public Room update(Room room) {
        ObjectChecker.check(room);
        ObjectChecker.checkIfExistsInDb(room, roomDao);

        logger.info("Updating room - {}", room);
        return roomDao.save(room);
    }

    @Override
    public void delete(Room room) {
        ObjectChecker.check(room);
        ObjectChecker.checkIfExistsInDb(room, roomDao);

        logger.info("Updating room - {}", room);
        roomDao.delete(room);
    }

}
