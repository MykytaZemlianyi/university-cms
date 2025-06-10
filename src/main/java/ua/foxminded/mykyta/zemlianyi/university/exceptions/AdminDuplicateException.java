package ua.foxminded.mykyta.zemlianyi.university.exceptions;

public class AdminDuplicateException extends UserDuplicateException {

    private static final long serialVersionUID = 1L;

    public AdminDuplicateException(String email) {
        super("Adimn", email);
    }
}
