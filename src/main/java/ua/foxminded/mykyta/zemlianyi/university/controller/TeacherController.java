package ua.foxminded.mykyta.zemlianyi.university.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import ua.foxminded.mykyta.zemlianyi.university.Constants;
import ua.foxminded.mykyta.zemlianyi.university.dto.*;
import ua.foxminded.mykyta.zemlianyi.university.service.*;

@Controller
public class TeacherController {
    private TeacherService teacherService;
    private CourseService courseService;

    public TeacherController(TeacherService teacherService, CourseService courseService) {
        this.teacherService = teacherService;
        this.courseService = courseService;
    }

    @GetMapping("/admin/teachers")
    public String getTeachers(@RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size, Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Teacher> teachers = teacherService.findAll(pageable);

        model.addAttribute("teachers", teachers);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", teachers.hasContent() ? teachers.getTotalPages() : 1);

        return "view-all-teachers";
    }

    @GetMapping("/admin/add-new-teacher")
    public String showCreateStudentForm(Model model) {
        List<Course> allCourses = courseService.findAll();
        model.addAttribute("teacher", new Teacher());
        model.addAttribute("courseList", allCourses);
        return "add-new-teacher";
    }

    @PostMapping("/admin/add-teacher")
    public String createTeacher(@RequestParam(required = false) List<Long> selectedCoursesId,
            @Valid @ModelAttribute Teacher teacher, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "add-new-teacher";
        }

        try {
            teacherService.resolveCourseFieldById(teacher, selectedCoursesId);
            teacherService.addNew(teacher);
            redirectAttributes.addFlashAttribute("successMessage", "Teacher added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/admin/teachers";
    }

    @GetMapping("/admin/edit-teacher/{id}")
    public String showEditTeacherForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Teacher> teacher = teacherService.findById(id);
        List<Course> allCourses = courseService.findAll();
        if (teacher.isPresent()) {
            model.addAttribute("teacher", teacher.get());
            model.addAttribute("courseList", allCourses);
            return "edit-teacher";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + Constants.USER_NOT_FOUND_ERROR);
            return "redirect:/admin/teachers";
        }
    }

    @PostMapping("/admin/edit-teacher/{id}")
    public String updateTeacher(@PathVariable Long id, @RequestParam(required = false) List<Long> selectedCoursesId,
            @Valid @ModelAttribute("teacher") Teacher updatedTeacher, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "edit-teacher";
        }

        try {
            teacherService.resolveCourseFieldById(updatedTeacher, selectedCoursesId);
            teacherService.update(updatedTeacher);
            redirectAttributes.addFlashAttribute("successMessage", "Teacher updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/admin/teachers";
    }

    @DeleteMapping("/admin/delete-teacher/{id}")
    public String deleteTeacher(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Teacher> teacher = teacherService.findById(id);
            if (teacher.isPresent()) {
                teacherService.delete(teacher.get());
                redirectAttributes.addFlashAttribute("successMessage", "Teacher deleted successfully!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Error: Teacher does not exists");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/admin/teachers";
    }
}
