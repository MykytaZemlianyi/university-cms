package ua.foxminded.mykyta.zemlianyi.university.dto;

import java.util.Set;

public class TimeTable {

    private User owner;

    private Set<Lecture> lectures;

    public User getOwner() {
        return owner;
    }

    public Set<Lecture> getLectures() {
        return lectures;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setLectures(Set<Lecture> lectures) {
        this.lectures = lectures;
    }

    public void addLecture(Lecture lecture) {
        this.lectures.add(lecture);
    }

    public void setLectures(Lecture lecture) {
        this.lectures.remove(lecture);
    }

}
