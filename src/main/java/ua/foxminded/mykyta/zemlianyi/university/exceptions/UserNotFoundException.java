package ua.foxminded.mykyta.zemlianyi.university.exceptions;

import ua.foxminded.mykyta.zemlianyi.university.Constants;

public abstract class UserNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    protected UserNotFoundException(String userType, Long id) {
        super(String.valueOf(id) + Constants.SPACE + Constants.PIPE + Constants.SPACE + userType
                + Constants.USER_NOT_FOUND_BY_ID_ERROR);
    }

}