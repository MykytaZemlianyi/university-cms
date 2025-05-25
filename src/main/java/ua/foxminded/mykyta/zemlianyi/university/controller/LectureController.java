package ua.foxminded.mykyta.zemlianyi.university.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import ua.foxminded.mykyta.zemlianyi.university.dto.Course;
import ua.foxminded.mykyta.zemlianyi.university.dto.Lecture;
import ua.foxminded.mykyta.zemlianyi.university.dto.LectureForm;
import ua.foxminded.mykyta.zemlianyi.university.dto.LectureType;
import ua.foxminded.mykyta.zemlianyi.university.dto.Room;
import ua.foxminded.mykyta.zemlianyi.university.service.CourseService;
import ua.foxminded.mykyta.zemlianyi.university.service.LectureService;
import ua.foxminded.mykyta.zemlianyi.university.service.RoomService;

@Controller
@RequestMapping("/lectures")
public class LectureController {
    private LectureService lectureService;
    private CourseService courseService;
    private RoomService roomService;

    public LectureController(LectureService lectureService, CourseService courseService, RoomService roomService) {
        this.lectureService = lectureService;
        this.courseService = courseService;
        this.roomService = roomService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String getLectures(@RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size, Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Lecture> lectures = lectureService.findAll(pageable);

        model.addAttribute("lectures", lectures);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", lectures.hasContent() ? lectures.getTotalPages() : 1);

        return "view-all-lectures";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String showCreateLectureForm(Model model) {
        List<Course> allCourses = courseService.findAll();
        List<Room> allRooms = roomService.findAll();
        model.addAttribute("lectureForm", new LectureForm());
        model.addAttribute("courseList", allCourses);
        model.addAttribute("roomList", allRooms);
        model.addAttribute("lectureTypes", LectureType.values());
        return "add-new-lecture";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String createLecture(@Valid @ModelAttribute LectureForm lectureForm, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "add-new-lecture";
        }

        Lecture lecture = lectureService.mapFormToLecture(lectureForm);
        lectureService.addNew(lecture);
        redirectAttributes.addFlashAttribute("successMessage", "Lecture added successfully!");

        return "redirect:/lectures";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String showEditLectureForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Lecture lecture = lectureService.getByIdOrThrow(id);
        LectureForm form = lectureService.mapLectureToForm(lecture);
        List<Course> allCourses = courseService.findAll();
        List<Room> allRooms = roomService.findAll();
        model.addAttribute("lectureForm", form);
        model.addAttribute("lectureTypes", LectureType.values());
        model.addAttribute("courseList", allCourses);
        model.addAttribute("roomList", allRooms);
        return "edit-lecture";

    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String updateLecture(@PathVariable Long id, @Valid @ModelAttribute("lectureForm") LectureForm form,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "edit-lecture";
        }

        Lecture lecture = lectureService.mapFormToLecture(form);
        lectureService.update(lecture);
        redirectAttributes.addFlashAttribute("successMessage", "Lecture updated successfully!");

        return "redirect:/lectures";
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String deleteLecture(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Lecture lecture = lectureService.getByIdOrThrow(id);
        lectureService.delete(lecture);
        redirectAttributes.addFlashAttribute("successMessage", "Lecture deleted successfully!");

        return "redirect:/lectures";
    }
}
