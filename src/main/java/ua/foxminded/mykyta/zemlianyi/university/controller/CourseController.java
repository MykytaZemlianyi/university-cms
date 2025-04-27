package ua.foxminded.mykyta.zemlianyi.university.controller;

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
public class CourseController {
    private CourseService courseService;
    private GroupService groupService;
    private TeacherService teacherService;

    public CourseController(CourseService courseService, GroupService groupService, TeacherService teacherService) {
        this.courseService = courseService;
        this.groupService = groupService;
        this.teacherService = teacherService;
    }

    @GetMapping("/admin/courses")
    public String getCourses(@RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size, Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Course> courses = courseService.findAll(pageable);

        model.addAttribute("courses", courses);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", courses.hasContent() ? courses.getTotalPages() : 1);

        return "view-all-courses";
    }

    @GetMapping("/admin/add-new-course")
    public String showCreateCourseForm(Model model) {
        List<Group> allGroups = groupService.findAll();
        List<Teacher> allTeachers = teacherService.findAll();
        model.addAttribute("course", new Course());
        model.addAttribute("groupList", allGroups);
        model.addAttribute("teacherList", allTeachers);
        return "add-new-course";
    }

    @PostMapping("/admin/add-course")
    public String createCourse(@Valid @ModelAttribute Course course, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "add-new-course";
        }

        try {
            courseService.addNew(course);
            redirectAttributes.addFlashAttribute("successMessage", "Course added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/admin/courses";
    }
}
