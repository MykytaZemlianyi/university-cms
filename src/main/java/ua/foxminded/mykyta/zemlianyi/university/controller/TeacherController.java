package ua.foxminded.mykyta.zemlianyi.university.controller;

import java.util.Collections;

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
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String getTeachers(@RequestParam(defaultValue = "0") Integer currentPage,
            @RequestParam(defaultValue = "5") Integer size, Model model) {

        Pageable pageable = PageRequest.of(currentPage, size);
        Page<Teacher> teachers = teacherService.findAll(pageable);

        model.addAttribute("teachers", teachers);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", teachers.hasContent() ? teachers.getTotalPages() : 1);

        return "view-all-teachers";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String showCreateTeacherForm(Model model, @RequestParam(defaultValue = "0") Integer coursePage,
            @RequestParam(defaultValue = "5") Integer coursePageSize) {
        prepareTeacherFormModel(model, coursePage, coursePageSize);
        model.addAttribute("teacher", new Teacher());
        return "add-new-teacher";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String createTeacher(@Valid @ModelAttribute Teacher teacher, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes, @RequestParam(defaultValue = "0") Integer coursePage,
            @RequestParam(defaultValue = "5") Integer coursePageSize) {

        if (bindingResult.hasErrors()) {
            prepareTeacherFormModel(model, coursePage, coursePageSize);
            return "add-new-teacher";
        }
        addSelectedIdsToModel(model);
        teacherService.addNew(teacher);
        redirectAttributes.addFlashAttribute("successMessage", "Teacher added successfully!");

        return "redirect:/teachers";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String showEditTeacherForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes,
            @RequestParam(defaultValue = "0") Integer coursePage,
            @RequestParam(defaultValue = "5") Integer coursePageSize) {

        Teacher teacher = teacherService.getByIdOrThrow(id);
        model.addAttribute("teacher", teacher);

        prepareTeacherFormModel(model, coursePage, coursePageSize);
        return "edit-teacher";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String updateTeacher(@PathVariable Long id, @Valid @ModelAttribute("teacher") Teacher updatedTeacher,
            BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model,
            @RequestParam(defaultValue = "0") Integer coursePage,
            @RequestParam(defaultValue = "5") Integer coursePageSize) {

        if (bindingResult.hasErrors()) {
            prepareTeacherFormModel(model, coursePage, coursePageSize);
            return "edit-teacher";
        }
        addSelectedIdsToModel(model);
        teacherService.update(updatedTeacher);
        redirectAttributes.addFlashAttribute("successMessage", "Teacher updated successfully!");
        return "redirect:/teachers";
    }

    private void prepareTeacherFormModel(Model model, @RequestParam(defaultValue = "0") Integer coursePage,
            @RequestParam(defaultValue = "5") Integer coursePageSize) {
        addSelectedIdsToModel(model);

        Pageable pageable = PageRequest.of(0, 5);
        Page<Course> coursePageObj = courseService.findAll(pageable);
        model.addAttribute("coursePage", coursePageObj);
    }

    private void addSelectedIdsToModel(Model model) {
        model.addAttribute("selectedCourseIds", Collections.emptySet());
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String deleteTeacher(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        teacherService.deleteByIdOrThrow(id);
        redirectAttributes.addFlashAttribute("successMessage", "Teacher deleted successfully!");
        return "redirect:/teachers";
    }
}
