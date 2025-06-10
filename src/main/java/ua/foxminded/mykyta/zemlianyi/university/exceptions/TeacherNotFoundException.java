package ua.foxminded.mykyta.zemlianyi.university.exceptions;

public class TeacherNotFoundException extends UserNotFoundException {
    private static final long serialVersionUID = 1L;

    public TeacherNotFoundException(Long id) {
        super("Teacher", id);
    }
}
