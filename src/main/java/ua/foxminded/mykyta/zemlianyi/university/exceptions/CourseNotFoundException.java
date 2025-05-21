package ua.foxminded.mykyta.zemlianyi.university.exceptions;

public class CourseNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public CourseNotFoundException(Long id) {
        super("Course with ID: " + id + " not found");
    }
}
