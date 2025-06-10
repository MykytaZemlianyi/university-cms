package ua.foxminded.mykyta.zemlianyi.university.exceptions;

import ua.foxminded.mykyta.zemlianyi.university.Constants;

public class CourseDuplicateException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CourseDuplicateException(String name) {
        super(name + Constants.COURSE_DUPLICATE_BY_NAME);
    }
}
