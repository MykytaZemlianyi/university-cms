package ua.foxminded.mykyta.zemlianyi.university.controller;

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
import ua.foxminded.mykyta.zemlianyi.university.Constants;
import ua.foxminded.mykyta.zemlianyi.university.dto.*;
import ua.foxminded.mykyta.zemlianyi.university.service.*;

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
    public String getTeachers(@RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size, Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Teacher> teachers = teacherService.findAll(pageable);

        model.addAttribute("teachers", teachers);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", teachers.hasContent() ? teachers.getTotalPages() : 1);

        return "view-all-teachers";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String showCreateStudentForm(Model model) {
        List<Course> allCourses = courseService.findAll();
        model.addAttribute("teacher", new Teacher());
        model.addAttribute("courseList", allCourses);
        return "add-new-teacher";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String createTeacher(@Valid @ModelAttribute Teacher teacher, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "add-new-teacher";
        }

        teacherService.addNew(teacher);
        redirectAttributes.addFlashAttribute("successMessage", "Teacher added successfully!");

        return "redirect:/teachers";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String showEditTeacherForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Teacher teacher = teacherService.getByIdOrThrow(id);
        List<Course> allCourses = courseService.findAll();
        model.addAttribute("teacher", teacher);
        model.addAttribute("courseList", allCourses);
        return "edit-teacher";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String updateTeacher(@PathVariable Long id, @Valid @ModelAttribute("teacher") Teacher updatedTeacher,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "edit-teacher";
        }

        teacherService.update(updatedTeacher);
        redirectAttributes.addFlashAttribute("successMessage", "Teacher updated successfully!");
        return "redirect:/teachers";
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String deleteTeacher(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Teacher teacher = teacherService.getByIdOrThrow(id);
        teacherService.delete(teacher);
        redirectAttributes.addFlashAttribute("successMessage", "Teacher deleted successfully!");
        return "redirect:/teachers";
    }
}
