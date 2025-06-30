package ua.foxminded.mykyta.zemlianyi.university.dto;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import ua.foxminded.mykyta.zemlianyi.university.Constants;

@Entity
@Table(name = "lectures", schema = "university")
public class Lecture implements Verifiable, Dto {
    @Id
    @Column(name = "lecture_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "lecture_type")
    private LectureType lectureType;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "room_id")
    private Room room;

    @Column(name = "lecture_time_start")
    private LocalDateTime timeStart;

    @Column(name = "lecture_time_end")
    private LocalDateTime timeEnd;

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

        if (room == null) {
            this.room = null;
        } else if (room.isAvailable(this)) {
            this.room = room;
        } else {
            throw new IllegalArgumentException(Constants.ROOM_NOT_AVIAILABLE + room.getId() + Constants.SPACE
                    + this.timeStart + Constants.PIPE + this.timeEnd);
        }
    }

    public void clearRelations() {
        if (this.room != null) {
            this.room.removeLecture(this);
        }
        if (this.course != null) {
            this.course.removeLecture(this);
        }
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

    public boolean isOverlappingWith(Lecture l2) {
        return this.timeStart.isBefore(l2.getTimeEnd()) && this.timeEnd.isAfter(l2.getTimeStart());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lectureType, timeEnd, timeStart);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Lecture other = (Lecture) obj;
        return Objects.equals(id, other.id) && lectureType == other.lectureType
                && Objects.equals(timeEnd, other.timeEnd) && Objects.equals(timeStart, other.timeStart);
    }

}
