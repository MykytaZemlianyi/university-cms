package ua.foxminded.mykyta.zemlianyi.university.exceptions;

public class StaffDuplicateException extends UserDuplicateException {

    private static final long serialVersionUID = 1L;

    public StaffDuplicateException(String email) {
        super("Staff", email);
    }
}
