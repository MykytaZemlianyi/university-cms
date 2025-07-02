package ua.foxminded.mykyta.zemlianyi.university.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
import ua.foxminded.mykyta.zemlianyi.university.dto.DatePicker;
import ua.foxminded.mykyta.zemlianyi.university.dto.DatePickerPreset;
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
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STUDENT','ROLE_TEACHER','ROLE_STAFF')")
    public String getLectures(@RequestParam(defaultValue = "0") Integer currentPage,
            @RequestParam(defaultValue = "5") Integer size, Model model) {
        Pageable pageable = PageRequest.of(currentPage, size);
        Page<Lecture> lectures = lectureService.findAll(pageable);

        model.addAttribute("lectures", lectures);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", lectures.hasContent() ? lectures.getTotalPages() : 1);

        return "view-all-lectures";
    }

    @GetMapping("/my-schedule")
    @PreAuthorize("hasAnyAuthority('ROLE_STUDENT','ROLE_TEACHER')")
    public String getMySchedule(@RequestParam(defaultValue = "0") Integer currentPage,
            @RequestParam(defaultValue = "5") Integer size, @RequestParam(required = false) DatePickerPreset preset,
            @RequestParam(required = false) LocalDate startDate, @RequestParam(required = false) LocalDate endDate,
            Model model) {

        DatePicker datePicker = new DatePicker(preset, startDate, endDate);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().findFirst()
                .map(Object::toString).orElse("").substring(5);

        Pageable pageable = PageRequest.of(currentPage, size);

        Page<Lecture> lectures = lectureService.findForUserByEmailInTimeInterval(username, role, datePicker, pageable);

        model.addAttribute("datePicker", datePicker);
        model.addAttribute("lectures", lectures);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", lectures.hasContent() ? lectures.getTotalPages() : 1);

        return "view-my-schedule";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public String showCreateLectureForm(Model model, @RequestParam(defaultValue = "0") Integer coursePage,
            @RequestParam(defaultValue = "5") Integer courseSize, @RequestParam(defaultValue = "0") Integer roomPage,
            @RequestParam(defaultValue = "5") Integer roomSize) {
        prepareModelForLectureForm(model, new LectureForm(), PageRequest.of(coursePage, courseSize),
                PageRequest.of(roomPage, roomSize));
        return "add-new-lecture";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public String createLecture(@Valid @ModelAttribute("lectureForm") LectureForm form, BindingResult bindingResult,
            RedirectAttributes redirectAttributes, @RequestParam(defaultValue = "0") Integer coursePage,
            @RequestParam(defaultValue = "5") Integer courseSize, @RequestParam(defaultValue = "0") Integer roomPage,
            @RequestParam(defaultValue = "5") Integer roomSize, Model model) {

        if (bindingResult.hasErrors()) {
            prepareModelForLectureForm(model, form, PageRequest.of(coursePage, courseSize),
                    PageRequest.of(roomPage, roomSize));
            return "add-new-lecture";
        }

        lectureService.addNewFromForm(form);
        redirectAttributes.addFlashAttribute("successMessage", "Lecture added successfully!");

        return "redirect:/lectures";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public String showEditLectureForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes,
            @RequestParam(defaultValue = "0") Integer coursePage, @RequestParam(defaultValue = "5") Integer courseSize,
            @RequestParam(defaultValue = "0") Integer roomPage, @RequestParam(defaultValue = "5") Integer roomSize) {
        Lecture lecture = lectureService.getByIdOrThrow(id);
        LectureForm form = lectureService.mapLectureToForm(lecture);
        prepareModelForLectureForm(model, form, PageRequest.of(coursePage, courseSize),
                PageRequest.of(roomPage, roomSize));
        return "edit-lecture";

    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public String updateLecture(@PathVariable Long id, @Valid @ModelAttribute("lectureForm") LectureForm form,
            BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model,
            @RequestParam(defaultValue = "0") Integer coursePage, @RequestParam(defaultValue = "5") Integer courseSize,
            @RequestParam(defaultValue = "0") Integer roomPage, @RequestParam(defaultValue = "5") Integer roomSize) {

        if (bindingResult.hasErrors()) {
            prepareModelForLectureForm(model, form, PageRequest.of(coursePage, courseSize),
                    PageRequest.of(roomPage, roomSize));
            return "edit-lecture";
        }

        lectureService.updateFromForm(form);
        redirectAttributes.addFlashAttribute("successMessage", "Lecture updated successfully!");

        return "redirect:/lectures";
    }

    private void prepareModelForLectureForm(Model model, LectureForm form, Pageable coursePageable,
            Pageable roomPageable) {
        Page<Course> coursesPageObj = courseService.findAll(coursePageable);
        Page<Room> roomsPageObj = roomService.findAll(roomPageable);

        model.addAttribute("lectureForm", form);
        model.addAttribute("lectureTypes", LectureType.values());
        model.addAttribute("coursePage", coursesPageObj);
        model.addAttribute("roomPage", roomsPageObj);
        model.addAttribute("selectedCourseId", form.getCourseId());
        model.addAttribute("selectedRoomId", form.getRoomId());
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String deleteLecture(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        lectureService.deleteByIdOrThrow(id);
        redirectAttributes.addFlashAttribute("successMessage", "Lecture deleted successfully!");

        return "redirect:/lectures";
    }
}
