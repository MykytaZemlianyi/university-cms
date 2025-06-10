package ua.foxminded.mykyta.zemlianyi.university.exceptions;

import ua.foxminded.mykyta.zemlianyi.university.Constants;

public class RoomDuplicateException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RoomDuplicateException(Integer number) {
        super(number + Constants.ROOM_DUPLICATE_BY_NUMBER);
    }
}
