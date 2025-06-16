package ua.foxminded.mykyta.zemlianyi.university.exceptions;

public class StaffNotFoundException extends UserNotFoundException {
    private static final long serialVersionUID = 1L;

    public StaffNotFoundException(Long id) {
        super("Staff", id);
    }
}
