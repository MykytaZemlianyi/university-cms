package ua.foxminded.mykyta.zemlianyi.university.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ua.foxminded.mykyta.zemlianyi.university.dao.RoomDao;
import ua.foxminded.mykyta.zemlianyi.university.dto.Room;
import ua.foxminded.mykyta.zemlianyi.university.exceptions.RoomDuplicateException;
import ua.foxminded.mykyta.zemlianyi.university.exceptions.RoomNotFoundException;

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
            throw new RoomDuplicateException(room.getNumber());
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
        ObjectChecker.checkNullAndId(newRoom);

        Room existingRoom = getByIdOrThrow(newRoom.getId());
        if (!Objects.equals(existingRoom.getNumber(), newRoom.getNumber())) {
            if (roomDao.existsByNumber(newRoom.getNumber())) {
                throw new RoomDuplicateException(existingRoom.getNumber());
            } else {
                existingRoom.setNumber(newRoom.getNumber());
            }
        }
        return existingRoom;

    }

    @Override
    public void deleteById(Room room) {
        ObjectChecker.checkNullAndVerify(room);
        ObjectChecker.checkIfExistsInDb(room, roomDao);
        logger.info("Deleting room - {}", room);
        roomDao.deleteById(room.getId());
    }

    @Override
    public void deleteByIdOrThrow(Long id) {
        Room room = getByIdOrThrow(id);
        logger.info("Deleting room - {}", room);
        roomDao.deleteById(id);
    }

    @Override
    public Page<Room> findAll(Pageable pageable) {
        return roomDao.findAll(pageable);
    }

    @Override
    public Optional<Room> findById(Long id) {
        return roomDao.findById(id);
    }

    @Override
    public List<Room> findAll() {
        return roomDao.findAll();
    }

    @Override
    public Room getByIdOrThrow(Long id) {
        return roomDao.findById(id).orElseThrow(() -> new RoomNotFoundException(id));
    }

}
