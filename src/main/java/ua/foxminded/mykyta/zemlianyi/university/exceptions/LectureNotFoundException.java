package ua.foxminded.mykyta.zemlianyi.university.exceptions;

public class LectureNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public LectureNotFoundException(Long id) {
        super("Lecture with ID: " + id + " not found");
    }
}
