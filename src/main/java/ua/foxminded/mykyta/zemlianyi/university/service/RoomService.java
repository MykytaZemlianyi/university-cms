package ua.foxminded.mykyta.zemlianyi.university.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ua.foxminded.mykyta.zemlianyi.university.dto.Room;

public interface RoomService {
    Room addNew(Room room);

    Room update(Room room);

    void delete(Room room);

    Page<Room> findAll(Pageable pageable);

    Optional<Room> findById(Long id);

    List<Room> findAll();

    Room getByIdOrThrow(Long id);
}
