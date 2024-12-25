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
public class Lecture {
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
}
