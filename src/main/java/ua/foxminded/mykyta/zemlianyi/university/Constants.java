package ua.foxminded.mykyta.zemlianyi.university;

public class Constants {
    private Constants() {
        throw new IllegalStateException("Utility Class");
    }

    public static final String EMAIL_PATTERN_REGEX = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";

    public static final String COURSE_OBJECT_INVALID_MSG = "Course is invalid";
    public static final String TEACHER_INVALID = "Teacher is invalid";
    public static final String FAIL_UPDATE_COURSE_DOES_NOT_EXIST = "Error during course update - course was not saved in the database earlier";

    public static final String STUDENT_INVALID = "Student is invalid";

    public static final String STUDENT_DOES_NOT_HAVE_GROUP = "Student does't have group";

}
