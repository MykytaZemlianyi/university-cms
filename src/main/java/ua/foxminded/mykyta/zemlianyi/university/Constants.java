package ua.foxminded.mykyta.zemlianyi.university;

import java.time.format.DateTimeFormatter;

public class Constants {
    private Constants() {
        throw new IllegalStateException("Utility Class");
    }

    public static final String NAME_SURNAME_VALIDATION_REGEX = "^[A-Z][A-Za-z' -]*$";
    public static final String PIPE = "|";
    public static final Object SPACE = " ";

    public static final String ROLE_PREFIX = "ROLE_";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_STAFF = "STAFF";
    public static final String ROLE_TEACHER = "TEACHER";
    public static final String ROLE_STUDENT = "STUDENT";

    public static final String LOAD_USER_BY_USERNAME = """
            SELECT email, "password", 'ROLE_ADMIN' AS role FROM university.admins WHERE email = :username
            UNION ALL
            SELECT email, "password", 'ROLE_STAFF' AS role FROM university.staff WHERE email = :username
            UNION ALL
            SELECT email, "password", 'ROLE_STUDENT' AS role FROM university.students WHERE email = :username
            UNION ALL
            SELECT email, "password", 'ROLE_TEACHER' AS role FROM university.teachers WHERE email = :username
            """;

    public static final String STUDENT_DOES_NOT_HAVE_GROUP = "Student doesn't have group";

    public static final String USERNAME_INVALID = "Username cannot be null or empty";
    public static final String USER_INVALID = " is invalid";
    public static final String USER_PASSWORD_CHANGE_ERROR = " doesn't exists in database - unable to change password";
    public static final String USER_DUPLICATE_BY_EMAIL_ERROR = " with this email already exists";
    public static final String USER_NOT_FOUND_BY_ID_ERROR = " with this ID does not exist in database";
    public static final String USER_NOT_FOUND_ERROR = "User not found";

    public static final String ROOM_LECTURE_OVERLAP_ERROR = "Lecture time is overlapping with existing lecture records";

    public static final String LECTURE_INVALID = "Lecture or its time attributes cannot be null";

    public static final String TIME_NULL = "One of the time stamps is null - ";
    public static final String TIME_INVALID_TIME_START_BEFORE_TIME_END = "Start time is before time end";
    public static final String TIME_INVALID_TIME_EQUAL_STAMPS = "Time start is equal to time end";

    public static final String OBJECT_INVALID_MSG = "Object is invalid - ";
    public static final String OBJECT_NULL = "Object is null";
    public static final String OBJECT_ID_NULL = "Object's ID is null";
    public static final String OBJECT_UPDATE_FAIL_DOES_NOT_EXIST = "Error during object update - object was not saved in the database earlier - ";
    public static final String COURSE_DUPLICATE_BY_NAME = " - course with the same name already exists";
    public static final String GROUP_DUPLICATE_BY_NAME = " - group with the same name already exists";
    public static final String ROOM_DUPLICATE_BY_NUMBER = " - room with the same number already exists";

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM");
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static final String INVALID_NAME_MSG = "Name should contain only letters and [',-]";
    public static final String INVALID_SURNAME_MSG = "Surname should contain only letters and [',-]";
    public static final String ROLE_INVALID = "Role cannot be null or empty";
}
