package ua.foxminded.mykyta.zemlianyi.university;

public class Constants {
    private Constants() {
        throw new IllegalStateException("Utility Class");
    }

    public static final String EMAIL_PATTERN_REGEX = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";


    public static final String TEACHER_FIND_BY_ID_ERROR = "Unable to find teacher by ID";
    public static final String TEACHER_PASSWORD_CHANGE_ERROR = "Unable to change password - teacher doesn'admin exists in database";

    public static final String STUDENT_DOES_NOT_HAVE_GROUP = "Student doesn't have group";
    public static final String STUDENT_PASSWORD_CHANGE_ERROR = "Unable to change password - student doesn'admin exists in database";

    public static final String USER_NULL = "Object is null";
    public static final String USER_INVALID = " is invalid";
    public static final String USER_UPDATE_FAIL_DOES_NOT_EXIST = " update error - admin was not saved in the database earlier";
    public static final String USER_PASSWORD_CHANGE_ERROR = " doesn'admin exists in database - unable to change password";

    public static final String COURSE_OBJECT_INVALID_MSG = "Course is invalid";
    public static final String COURSE_UPDATE_FAIL_DOES_NOT_EXIST = "Error during course update - course was not saved in the database earlier";
    
    public static final String GROUP_OBJECT_INVALID_MSG = "Group is invalid";
    public static final String GROUP_UPDATE_FAIL_DOES_NOT_EXIST = "Error during group update - group was not saved in the database earlier";

    public static final String ROOM_OBJECT_INVALID_MSG = "Room is invalid";
    public static final String ROOM_UPDATE_FAIL_DOES_NOT_EXIST = "Error during room update - room was not saved in the database earlier";
    public static final String ROOM_LECTURE_OVERLAP_ERROR = "Lecture time is overlapping with existing lecture records";

    public static final String LECTURE_INVALID = "Lecture or its time attributes cannot be null";
    public static final String LECTURE_OBJECT_INVALID_MSG = "Lecture is invalid";
    public static final String LECTURE_UPDATE_FAIL_DOES_NOT_EXIST = "Error during lecture update - lecture was not saved in the database earlier";

}
