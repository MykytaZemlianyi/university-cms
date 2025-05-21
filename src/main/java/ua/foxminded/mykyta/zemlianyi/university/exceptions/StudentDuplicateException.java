package ua.foxminded.mykyta.zemlianyi.university.exceptions;

import ua.foxminded.mykyta.zemlianyi.university.Constants;

public class StudentDuplicateException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public StudentDuplicateException(String email) {
        super(email + Constants.USER_SAVE_ERROR_EMAIL_EXISTS);
    }
}
