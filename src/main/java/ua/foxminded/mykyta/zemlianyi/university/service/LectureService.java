package ua.foxminded.mykyta.zemlianyi.university.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ua.foxminded.mykyta.zemlianyi.university.dto.Course;
import ua.foxminded.mykyta.zemlianyi.university.dto.DatePicker;
import ua.foxminded.mykyta.zemlianyi.university.dto.Lecture;
import ua.foxminded.mykyta.zemlianyi.university.dto.LectureForm;

public interface LectureService {
    Lecture addNew(Lecture lecture);

    Lecture addNewFromForm(LectureForm form);

    Lecture update(Lecture lecture);

    Lecture updateFromForm(LectureForm form);

    List<Lecture> findForCourse(Course course);

    List<Lecture> findForCourseInTimeInterval(Course course, LocalDateTime timeStart, LocalDateTime timeEnd);

    Page<Lecture> findForUserByEmailInTimeInterval(String email, String role, DatePicker datePicker, Pageable pageable);

    void deleteById(Lecture lecture);

    Page<Lecture> findAll(Pageable pageable);

    Lecture mapFormToLecture(LectureForm form);

    Optional<Lecture> findById(Long id);

    LectureForm mapLectureToForm(Lecture lecture);

    Lecture getByIdOrThrow(Long id);

    void deleteByIdOrThrow(Long id);

}
