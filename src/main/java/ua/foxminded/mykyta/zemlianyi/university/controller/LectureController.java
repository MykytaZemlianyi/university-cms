package ua.foxminded.mykyta.zemlianyi.university.controller;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import ua.foxminded.mykyta.zemlianyi.university.dto.*;
import ua.foxminded.mykyta.zemlianyi.university.service.*;

@Controller
public class LectureController {
    private LectureService lectureService;
    private CourseService courseService;
    private RoomService roomService;

    public LectureController(LectureService lectureService, CourseService courseService, RoomService roomService) {
        this.lectureService = lectureService;
        this.courseService = courseService;
        this.roomService = roomService;
    }

    @GetMapping("/admin/lectures")
    public String getLectures(@RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size, Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Lecture> lectures = lectureService.findAll(pageable);

        model.addAttribute("lectures", lectures);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", lectures.hasContent() ? lectures.getTotalPages() : 1);

        return "view-all-lectures";
    }

    @GetMapping("/admin/add-lecture")
    public String showCreateLectureForm(Model model) {
        List<Course> allCourses = courseService.findAll();
        List<Room> allRooms = roomService.findAll();
        model.addAttribute("lectureForm", new LectureForm());
        model.addAttribute("courseList", allCourses);
        model.addAttribute("roomList", allRooms);
        model.addAttribute("lectureTypes", LectureType.values());
        model.addAttribute("currentDate", LocalDate.now());
        return "add-new-lecture";
    }

    @PostMapping("/admin/add-lecture")
    public String createLecture(@Valid @ModelAttribute LectureForm lectureForm, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "add-new-lecture";
        }

        try {
            Lecture lecture = lectureService.mapFormToLecture(lectureForm);
            lectureService.addNew(lecture);
            redirectAttributes.addFlashAttribute("successMessage", "Lecture added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/admin/lectures";
    }

}
