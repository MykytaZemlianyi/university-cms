package ua.foxminded.mykyta.zemlianyi.university.controller;

import java.util.Collections;
import java.util.List;
import java.util.Set;

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
import ua.foxminded.mykyta.zemlianyi.university.dto.*;
import ua.foxminded.mykyta.zemlianyi.university.service.*;

@Controller
@RequestMapping("/courses")
public class CourseController {
    private CourseService courseService;
    private TeacherService teacherService;

    public CourseController(CourseService courseService, TeacherService teacherService) {
        this.courseService = courseService;
        this.teacherService = teacherService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String getCourses(@RequestParam(defaultValue = "0") Integer currentPage,
            @RequestParam(defaultValue = "5") Integer size, Model model) {

        Pageable pageable = PageRequest.of(currentPage, size);
        Page<Course> courses = courseService.findAll(pageable);

        model.addAttribute("courses", courses);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", courses.hasContent() ? courses.getTotalPages() : 1);

        return "view-all-courses";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String showCreateCourseForm(@RequestParam(defaultValue = "0") Integer teacherPage,
            @RequestParam(defaultValue = "5") Integer teacherSize, Model model) {

        prepareModelForCourseForm(model, new Course(), PageRequest.of(teacherPage, teacherSize));
        return "add-new-course";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String createCourse(@Valid @ModelAttribute Course course, BindingResult bindingResult,
            RedirectAttributes redirectAttributes, @RequestParam(defaultValue = "0") Integer teacherPage,
            @RequestParam(defaultValue = "5") Integer teacherSize, Model model) {

        if (bindingResult.hasErrors()) {
            prepareModelForCourseForm(model, course, PageRequest.of(teacherPage, teacherSize));
            return "add-new-course";
        }

        courseService.addNew(course);
        redirectAttributes.addFlashAttribute("successMessage", "Course added successfully!");
        return "redirect:/courses";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String showEditCourseForm(@PathVariable Long id, Model model,
            @RequestParam(defaultValue = "0") Integer teacherPage,
            @RequestParam(defaultValue = "5") Integer teacherSize) {
        Course course = courseService.getByIdOrThrow(id);
        prepareModelForCourseForm(model, course, PageRequest.of(teacherPage, teacherSize));
        return "edit-course";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String updateCourse(@PathVariable Long id, @Valid @ModelAttribute("course") Course updatedCourse,
            BindingResult bindingResult, RedirectAttributes redirectAttributes,
            @RequestParam(defaultValue = "0") Integer teacherPage,
            @RequestParam(defaultValue = "5") Integer teacherSize, Model model) {

        if (bindingResult.hasErrors()) {
            prepareModelForCourseForm(model, updatedCourse, PageRequest.of(teacherPage, teacherSize));
            return "edit-course";
        }

        courseService.update(updatedCourse);
        redirectAttributes.addFlashAttribute("successMessage", "Course updated successfully!");
        return "redirect:/courses";
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String deleteCourse(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        courseService.deleteOrThrow(id);
        redirectAttributes.addFlashAttribute("successMessage", "Course deleted successfully!");

        return "redirect:/courses";
    }

    private void prepareModelForCourseForm(Model model, Course course, Pageable pageable) {
        Page<Teacher> teacherPage = teacherService.findAll(pageable);
        model.addAttribute("course", course);
        model.addAttribute("teacherPage", teacherPage);
        model.addAttribute("selectedTeacherId", course.getTeacher() != null ? course.getTeacher().getId() : null);
    }

    @PostMapping("/courseSelectCheckboxList")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String getCourseSelectCheckboxList(@ModelAttribute Group group,
            @RequestParam(defaultValue = "0") Integer currentPage, @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(required = false) Set<Long> selectedCourseIds, Model model) {

        if (selectedCourseIds == null) {
            selectedCourseIds = Collections.emptySet();
        }

        Pageable pageable = PageRequest.of(currentPage, size);
        Page<Course> coursePage = courseService.findAll(pageable);

        model.addAttribute("coursePage", coursePage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", coursePage.getTotalPages());
        model.addAttribute("selectedCourseIds", selectedCourseIds);
        model.addAttribute("group", group);

        return "fragments/course_fragments :: courseSelectCheckboxList";
    }
}
