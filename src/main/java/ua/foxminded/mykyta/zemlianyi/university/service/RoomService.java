package ua.foxminded.mykyta.zemlianyi.university.service;

import ua.foxminded.mykyta.zemlianyi.university.dto.Room;

public interface RoomService {
    Room addNew(Room room);

    Room update(Room room);

    void delete(Room room);
}
