package ua.foxminded.mykyta.zemlianyi.university.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ua.foxminded.mykyta.zemlianyi.university.dto.*;
import ua.foxminded.mykyta.zemlianyi.university.service.*;

@Controller
public class CourseController {
    private CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
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

}
