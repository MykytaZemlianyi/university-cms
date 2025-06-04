package ua.foxminded.mykyta.zemlianyi.university.exceptions;

public class StudentDuplicateException extends UserDuplicateException {

    private static final long serialVersionUID = 1L;

    public StudentDuplicateException(String email) {
        super("Student", email);
    }
}
