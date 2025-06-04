package ua.foxminded.mykyta.zemlianyi.university.exceptions;

public class StudentNotFoundException extends UserNotFoundException {
    private static final long serialVersionUID = 1L;

    public StudentNotFoundException(Long id) {
        super("Student", id);
    }
}
