package ua.foxminded.mykyta.zemlianyi.university.dto;

public enum LectureType {
    LECTURE("Lecture", "Lec"), LABORATORIUM("Laboratorium", "Lab"), SEMINAR("Seminar", "Sem");

    final String toString;
    final String toShortString;

    LectureType(String toString, String toShortString) {
        this.toString = toString;
        this.toShortString = toShortString;
    }
}
