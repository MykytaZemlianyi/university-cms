package ua.foxminded.mykyta.zemlianyi.university.exceptions;

public class AdminNotFoundException extends UserNotFoundException {
    private static final long serialVersionUID = 1L;

    public AdminNotFoundException(Long id) {
        super("Admin", id);
    }
}
