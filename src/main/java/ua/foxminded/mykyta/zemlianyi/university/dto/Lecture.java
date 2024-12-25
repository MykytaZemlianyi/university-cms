package ua.foxminded.mykyta.zemlianyi.university.dto;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Lecture implements Verifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lecture_type")
    private LectureType lectureType;

    // TODO Not sure about this relation
    @Column(name = "course_id")
    private Course course;

    @Column(name = "lecture_time_start")
    private LocalDateTime timeStart;

    @Column(name = "lecture_time_end")
    private LocalDateTime timeEnd;

    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "room_id")
    private Room room;

    public Long getId() {
        return id;
    }

    public LectureType getLectureType() {
        return lectureType;
    }

    public Course getCourse() {
        return course;
    }

    public LocalDateTime getTimeStart() {
        return timeStart;
    }

    public LocalDateTime getTimeEnd() {
        return timeEnd;
    }

    public Room getRoom() {
        return room;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLectureType(LectureType lectureType) {
        this.lectureType = lectureType;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setTimeStart(LocalDateTime timeStart) {
        this.timeStart = timeStart;
    }

    public void setTimeEnd(LocalDateTime timeEnd) {
        this.timeEnd = timeEnd;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public boolean verify() {
        return verifyType() && verifyCourse() && verifyTime();
    }

    public boolean verifyType() {
        return this.lectureType != null;
    }

    public boolean verifyCourse() {
        return this.course != null;
    }

    public boolean verifyTime() {
        if (verifyTimeStart() && verifyTimeEnd()) {
            return this.timeStart.isBefore(timeEnd);
        } else {
            return false;
        }
    }

    public boolean verifyTimeStart() {
        return this.timeStart != null;
    }

    public boolean verifyTimeEnd() {
        return this.timeEnd != null;
    }

}
