package ua.foxminded.mykyta.zemlianyi.university.exceptions;

import ua.foxminded.mykyta.zemlianyi.university.Constants;

public abstract class UserDuplicateException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    protected UserDuplicateException(String userType, String email) {
        super(email + Constants.SPACE + Constants.PIPE + Constants.SPACE + userType
                + Constants.USER_DUPLICATE_BY_EMAIL_ERROR);
    }
}
