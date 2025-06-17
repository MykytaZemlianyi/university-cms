package ua.foxminded.mykyta.zemlianyi.university.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
import ua.foxminded.mykyta.zemlianyi.university.dto.Teacher;
import ua.foxminded.mykyta.zemlianyi.university.service.CourseService;
import ua.foxminded.mykyta.zemlianyi.university.service.TeacherService;

@Controller
@RequestMapping("/teachers")
public class TeacherController {
    private TeacherService teacherService;
    private CourseService courseService;

    public TeacherController(TeacherService teacherService, CourseService courseService) {
        this.teacherService = teacherService;
        this.courseService = courseService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STUDENT','ROLE_TEACHER','ROLE_STAFF')")
    public String getTeachers(@RequestParam(defaultValue = "0") Integer currentPage,
            @RequestParam(defaultValue = "5") Integer size, Model model) {

        Pageable pageable = PageRequest.of(currentPage, size);
        Page<Teacher> teachers = teacherService.findAll(pageable);

        model.addAttribute("teachers", teachers);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", teachers.hasContent() ? teachers.getTotalPages() : 1);

        return "view-all-teachers";
    }

    private void prepareTeacherFormModel(Model model, Teacher teacher, Pageable pageable) {

        Page<Course> coursePageObj = courseService.findAll(pageable);
        model.addAttribute("coursePage", coursePageObj);
        model.addAttribute("teacher", teacher);

        List<Long> selectedCourseIds = Optional.ofNullable(teacher.getCourses().stream().map(Course::getId).toList())
                .orElse(Collections.emptyList());
        model.addAttribute("selectedCourseIds", selectedCourseIds);
    }

    @GetMapping("/add")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String showCreateTeacherForm(Model model, @RequestParam(defaultValue = "0") Integer coursePage,
            @RequestParam(defaultValue = "5") Integer coursePageSize) {
        prepareTeacherFormModel(model, new Teacher(), PageRequest.of(coursePage, coursePageSize));
        return "add-new-teacher";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String createTeacher(@Valid @ModelAttribute Teacher teacher, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes, @RequestParam(defaultValue = "0") Integer coursePage,
            @RequestParam(defaultValue = "5") Integer coursePageSize) {

        if (bindingResult.hasErrors()) {
            prepareTeacherFormModel(model, teacher, PageRequest.of(coursePage, coursePageSize));
            return "add-new-teacher";
        }
        teacherService.addNew(teacher);
        redirectAttributes.addFlashAttribute("successMessage", "Teacher added successfully!");

        return "redirect:/teachers";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public String showEditTeacherForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes,
            @RequestParam(defaultValue = "0") Integer coursePage,
            @RequestParam(defaultValue = "5") Integer coursePageSize) {

        Teacher teacher = teacherService.getByIdOrThrow(id);
        prepareTeacherFormModel(model, teacher, PageRequest.of(coursePage, coursePageSize));
        return "edit-teacher";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public String updateTeacher(@PathVariable Long id, @Valid @ModelAttribute("teacher") Teacher updatedTeacher,
            BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model,
            @RequestParam(defaultValue = "0") Integer coursePage,
            @RequestParam(defaultValue = "5") Integer coursePageSize) {

        if (bindingResult.hasErrors()) {
            prepareTeacherFormModel(model, updatedTeacher, PageRequest.of(coursePage, coursePageSize));
            return "edit-teacher";
        }
        teacherService.update(updatedTeacher);
        redirectAttributes.addFlashAttribute("successMessage", "Teacher updated successfully!");
        return "redirect:/teachers";
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String deleteTeacher(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        teacherService.deleteOrThrow(id);
        redirectAttributes.addFlashAttribute("successMessage", "Teacher deleted successfully!");
        return "redirect:/teachers";
    }

    @PostMapping("/teacherSelectRadioList")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public String getTeacherRadioList(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size, @RequestParam(required = false) Long selectedTeacherId,
            Model model) {
        Page<Teacher> teacherPage = teacherService.findAll(PageRequest.of(page, size));
        model.addAttribute("teacherPage", teacherPage);
        model.addAttribute("selectedTeacherId", selectedTeacherId);
        return "fragments/teacher_fragments :: teacherSelectRadioList";
    }
}
