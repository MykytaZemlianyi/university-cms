package ua.foxminded.mykyta.zemlianyi.university.exceptions;

public class RoomNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public RoomNotFoundException(Long id) {
        super("Room with ID: " + id + " not found");
    }
}
