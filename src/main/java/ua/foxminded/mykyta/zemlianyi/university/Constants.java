package ua.foxminded.mykyta.zemlianyi.university;

import java.time.format.DateTimeFormatter;

public class Constants {
    private Constants() {
        throw new IllegalStateException("Utility Class");
    }

    public static final String EMAIL_PATTERN_REGEX = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
    public static final String PIPE = "|";
    public static final Object SPACE = " ";

    public static final String ROLE_PREFIX = "ROLE_";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_TEACHER = "TEACHER";
    public static final String ROLE_STUDENT = "STUDENT";

    public static final String STUDENT_DOES_NOT_HAVE_GROUP = "Student doesn't have group";

    public static final String USER_INVALID = " is invalid";
    public static final String USER_PASSWORD_CHANGE_ERROR = " doesn't exists in database - unable to change password";
    public static final String USER_SAVE_ERROR_EMAIL_EXISTS = " - user with this email already exists";
    public static final String USER_NOT_FOUND_ERROR = "User not found";

    public static final String ROOM_LECTURE_OVERLAP_ERROR = "Lecture time is overlapping with existing lecture records";

    public static final String LECTURE_INVALID = "Lecture or its time attributes cannot be null";

    public static final String TIME_NULL = "One of the time stamps is null - ";
    public static final String TIME_INVALID_TIME_START_BEFORE_TIME_END = "Start time is before time end";
    public static final String TIME_INVALID_TIME_EQUAL_STAMPS = "Time start is equal to time end";

    public static final String OBJECT_INVALID_MSG = "Object is invalid - ";
    public static final String OBJECT_UPDATE_FAIL_DOES_NOT_EXIST = "Error during object update - object was not saved in the database earlier - ";
    public static final String COURSE_ADD_NEW_ERROR_EXISTS_BY_NAME = " - course with the same name already exists";
    public static final String GROUP_ADD_NEW_ERROR_EXISTS_BY_NAME = " - group with the same name already exists";
    public static final String ROOM_ADD_NEW_ERROR_EXISTS_BY_NUMBER = " - room with the same number already exists";

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM");
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
}
