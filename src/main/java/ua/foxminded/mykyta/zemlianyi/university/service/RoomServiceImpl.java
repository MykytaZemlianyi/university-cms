package ua.foxminded.mykyta.zemlianyi.university.service;

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
        ObjectChecker.check(room);
        if (roomDao.existsByNumber(room.getNumber())) {
            throw new IllegalArgumentException(room.getNumber() + Constants.ROOM_ADD_NEW_ERROR_EXISTS_BY_NUMBER);
        }
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

    @Override
    public Page<Room> findAll(Pageable pageable) {
        return roomDao.findAll(pageable);
    }

}
