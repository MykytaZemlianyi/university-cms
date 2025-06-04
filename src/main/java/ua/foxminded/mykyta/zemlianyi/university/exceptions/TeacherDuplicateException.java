package ua.foxminded.mykyta.zemlianyi.university.exceptions;

public class TeacherDuplicateException extends UserDuplicateException {

    private static final long serialVersionUID = 1L;

    public TeacherDuplicateException(String email) {
        super("Teacher", email);
    }
}
