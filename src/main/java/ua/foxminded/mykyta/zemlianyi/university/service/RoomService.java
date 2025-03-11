package ua.foxminded.mykyta.zemlianyi.university.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ua.foxminded.mykyta.zemlianyi.university.dto.Room;

public interface RoomService {
    Room addNew(Room room);

    Room update(Room room);

    void delete(Room room);

    Page<Room> findAll(Pageable pageable);
}
