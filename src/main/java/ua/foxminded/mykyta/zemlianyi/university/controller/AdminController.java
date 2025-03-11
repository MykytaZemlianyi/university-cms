package ua.foxminded.mykyta.zemlianyi.university.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ua.foxminded.mykyta.zemlianyi.university.Constants;
import ua.foxminded.mykyta.zemlianyi.university.dto.*;
import ua.foxminded.mykyta.zemlianyi.university.service.*;

@Controller
public class AdminController {
    private GroupService groupService;
    private StudentService studentService;
    private AdminService adminService;
    private TeacherService teacherService;
    private CourseService courseService;
    private LectureService lectureService;

    public AdminController(GroupService groupService, StudentService studentService, AdminService adminService,
            TeacherService teacherService, CourseService courseService, LectureService lectureService) {
        this.groupService = groupService;
        this.studentService = studentService;
        this.adminService = adminService;
        this.teacherService = teacherService;
        this.courseService = courseService;
        this.lectureService = lectureService;
    }

    @GetMapping("/admin/groups")
    public String getGroups(@RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size, Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Group> groups = groupService.findAll(pageable);

        model.addAttribute("groups", groups);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", groups.getTotalPages());

        return "admin/groups";
    }

    @GetMapping("admin/students")
    public String getStudents(@RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size, Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Student> students = studentService.findAll(pageable);

        LinkedHashMap<String, Function<Student, Object>> columnData = new LinkedHashMap<>();
        columnData.put("ID", Student::getId);
        columnData.put("Name", Student::getName);
        columnData.put("Surname", Student::getSurname);
        columnData.put("Email", Student::getEmail);
        columnData.put("Group", student -> student.getGroup() != null ? student.getGroup().getName() : "No group");

        model.addAttribute("students", students);
        model.addAttribute("columns", columnData);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", students.hasContent() ? students.getTotalPages() : 1);

        return "admin/students";
    }

    @GetMapping("admin/admins")
    public String getAdmins(@RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size, Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Admin> admins = adminService.findAll(pageable);

        LinkedHashMap<String, Function<Admin, Object>> columnData = new LinkedHashMap<>();
        columnData.put("ID", Admin::getId);
        columnData.put("Name", Admin::getName);
        columnData.put("Surname", Admin::getSurname);
        columnData.put("Email", Admin::getEmail);

        model.addAttribute("admins", admins);
        model.addAttribute("columns", columnData);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", admins.hasContent() ? admins.getTotalPages() : 1);

        return "admin/admins";
    }

    @GetMapping("admin/teachers")
    public String getTeachers(@RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size, Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Teacher> teachers = teacherService.findAll(pageable);

        LinkedHashMap<String, Function<Teacher, Object>> columnData = new LinkedHashMap<>();
        columnData.put("ID", Teacher::getId);
        columnData.put("Name", Teacher::getName);
        columnData.put("Surname", Teacher::getSurname);
        columnData.put("Email", Teacher::getEmail);

        model.addAttribute("teachers", teachers);
        model.addAttribute("columns", columnData);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", teachers.hasContent() ? teachers.getTotalPages() : 1);

        return "admin/teachers";
    }

    @GetMapping("admin/courses")
    public String getCourses(@RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size, Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Course> courses = courseService.findAll(pageable);

        LinkedHashMap<String, Function<Course, Object>> columnData = new LinkedHashMap<>();
        columnData.put("ID", Course::getId);
        columnData.put("Name", Course::getName);
        columnData.put("Teacher", course -> {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(course.getTeacher().getName());
            stringBuilder.append(Constants.SPACE);
            stringBuilder.append(course.getTeacher().getSurname());
            return stringBuilder.toString();
        });
        columnData.put("Groups", course -> {
            StringBuilder stringBuilder = new StringBuilder();
            course.getGroups().stream().forEach(group -> {
                if (!stringBuilder.isEmpty()) {
                    stringBuilder.append(Constants.SPACE);
                    stringBuilder.append(Constants.PIPE);
                    stringBuilder.append(Constants.SPACE);
                }
                stringBuilder.append(group.getName());
            });
            return stringBuilder.toString();
        });

        model.addAttribute("courses", courses);
        model.addAttribute("columns", columnData);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", courses.hasContent() ? courses.getTotalPages() : 1);

        return "admin/courses";
    }

    @GetMapping("admin/lectures")
    public String getLectures(@RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size, Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Lecture> lectures = lectureService.findAll(pageable);

        LinkedHashMap<String, Function<Lecture, Object>> columnData = new LinkedHashMap<>();

        columnData.put("ID", Lecture::getId);
        columnData.put("Date", lecture -> lecture.getTimeStart().format(Constants.DATE_FORMATTER));
        columnData.put("Begins", lecture -> lecture.getTimeStart().format(Constants.TIME_FORMATTER));
        columnData.put("Ends", lecture -> lecture.getTimeEnd().format(Constants.TIME_FORMATTER));
        columnData.put("Type", Lecture::getLectureType);
        columnData.put("Course", lecture -> lecture.getCourse().getName());
        columnData.put("Teacher", lecture -> {
            Course course = lecture.getCourse();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(course.getTeacher().getName());
            stringBuilder.append(Constants.SPACE);
            stringBuilder.append(course.getTeacher().getSurname());
            return stringBuilder.toString();
        });

        columnData.put("Groups", lecture -> {
            StringBuilder stringBuilder = new StringBuilder();
            lecture.getCourse().getGroups().stream().forEach(group -> {
                if (!stringBuilder.isEmpty()) {
                    stringBuilder.append(Constants.SPACE);
                    stringBuilder.append(Constants.PIPE);
                    stringBuilder.append(Constants.SPACE);
                }
                stringBuilder.append(group.getName());
            });
            return stringBuilder.toString();
        });

        model.addAttribute("lectures", lectures);
        model.addAttribute("columns", columnData);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", lectures.hasContent() ? lectures.getTotalPages() : 1);

        return "admin/lectures";
    }
}
