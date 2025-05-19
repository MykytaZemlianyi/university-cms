package ua.foxminded.mykyta.zemlianyi.university.exceptions;

public class AdminNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public AdminNotFoundException(Long id) {
        super("Admin with ID: " + id + " not found");
    }
}
