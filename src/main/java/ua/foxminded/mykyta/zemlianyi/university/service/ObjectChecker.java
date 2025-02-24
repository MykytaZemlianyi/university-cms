package ua.foxminded.mykyta.zemlianyi.university.service;

import java.time.LocalDateTime;

import org.springframework.data.repository.CrudRepository;

import ua.foxminded.mykyta.zemlianyi.university.Constants;
import ua.foxminded.mykyta.zemlianyi.university.dto.Dto;
import ua.foxminded.mykyta.zemlianyi.university.dto.Verifiable;

public class ObjectChecker {
    private ObjectChecker() {
        throw new IllegalArgumentException("Utility class");
    }

    public static <T extends Verifiable> void check(T object) {
        if (object == null || !object.verify()) {
            throw new IllegalArgumentException(Constants.OBJECT_INVALID_MSG + object);
        }
    }

    public static void checkInterval(LocalDateTime timeStart, LocalDateTime timeEnd) {
        if (timeStart == null || timeEnd == null) {
            throw new IllegalArgumentException(Constants.TIME_NULL + timeStart + Constants.PIPE + timeEnd);
        }
        if (timeEnd.isBefore(timeStart)) {
            throw new IllegalArgumentException(Constants.TIME_INVALID_TIME_START_BEFORE_TIME_END);
        }
        if (timeStart.equals(timeEnd)) {
            throw new IllegalArgumentException(Constants.TIME_INVALID_TIME_EQUAL_STAMPS);
        }
    }

    public static <T extends Dto> void checkIfExistsInDb(T object, CrudRepository<T, Long> dao) {
        if (!dao.existsById(object.getId())) {
            throw new IllegalArgumentException(Constants.OBJECT_UPDATE_FAIL_DOES_NOT_EXIST);
        }
    }

}
