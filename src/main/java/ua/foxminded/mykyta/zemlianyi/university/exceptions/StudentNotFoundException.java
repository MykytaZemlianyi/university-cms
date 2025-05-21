package ua.foxminded.mykyta.zemlianyi.university.exceptions;

public class StudentNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public StudentNotFoundException(Long id) {
        super("Student with ID: " + id + " not found");
    }
}
