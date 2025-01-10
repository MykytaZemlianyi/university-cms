package ua.foxminded.mykyta.zemlianyi.university;

public class Constants {
    private Constants() {
        throw new IllegalStateException("Utility Class");
    }

    public static final String EMAIL_PATTERN_REGEX = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
    public static final String FIND_COURSES_BY_TEACHER = "SELECT c FROM Course c WHERE c.teacher_id = :teacherId";
}
