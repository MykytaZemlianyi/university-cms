package ua.foxminded.mykyta.zemlianyi.university.dto;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import ua.foxminded.mykyta.zemlianyi.university.Constants;

@Entity
@Table(name = "rooms", schema = "university")
public class Room implements Verifiable {
    @Id
    @Column(name = "room_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "room_number")
    private Integer number;
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private Set<Lecture> lectures = new HashSet<>();

    public Long getId() {
        return id;
    }

    public Integer getNumber() {
        return number;
    }

    public Set<Lecture> getLectures() {
        return lectures;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public void setLectures(Set<Lecture> lectures) {
        this.lectures = lectures;
    }

    public void addLecture(Lecture lecture) {
        if (isAvailable(lecture)) {
            this.lectures.add(lecture);
            lecture.setRoom(this);
        } else {
            throw new IllegalArgumentException(Constants.ROOM_LECTURE_OVERLAP_ERROR);
        }
    }

    public void removeLecture(Lecture lecture) {
        this.lectures.remove(lecture);
        lecture.setRoom(null);
    }

    public boolean isAvailable(Lecture lecture) {
        if (lecture == null || !lecture.verify()) {
            throw new IllegalArgumentException(Constants.LECTURE_INVALID);
        }

        for (Lecture existingLecture : lectures) {
            if (isOverlapping(existingLecture, lecture)) {
                return false;
            }
        }
        return true;
    }

    private boolean isOverlapping(Lecture l1, Lecture l2) {
        return (l1.getTimeEnd().isAfter(l2.getTimeStart()) || l1.getTimeStart().isBefore(l2.getTimeEnd()));
    }

    @Override
    public boolean verify() {
        return this.number != null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Room other = (Room) obj;
        return Objects.equals(id, other.id) && Objects.equals(number, other.number);
    }

}
