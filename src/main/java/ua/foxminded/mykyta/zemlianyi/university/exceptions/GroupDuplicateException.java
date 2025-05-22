package ua.foxminded.mykyta.zemlianyi.university.exceptions;

import ua.foxminded.mykyta.zemlianyi.university.Constants;

public class GroupDuplicateException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public GroupDuplicateException(String name) {
        super(name + Constants.GROUP_DUPLICATE_BY_NAME);
    }
}
