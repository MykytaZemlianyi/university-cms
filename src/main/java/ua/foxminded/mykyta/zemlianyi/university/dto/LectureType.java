package ua.foxminded.mykyta.zemlianyi.university.dto;

public enum LectureType {
    LECTURE("Lecture", "Lec"), LABORATORIUM("Laboratorium", "Lab"), SEMINAR("Seminar", "Sem");

    private final String fullName;
    private final String shortName;

    LectureType(String fullName, String shortName) {
        this.fullName = fullName;
        this.shortName = shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getShortName() {
        return shortName;
    }
}
