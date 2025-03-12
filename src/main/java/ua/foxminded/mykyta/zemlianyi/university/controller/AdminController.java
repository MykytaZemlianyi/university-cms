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
public class AdminController {
    private GroupService groupService;
    private StudentService studentService;
    private AdminService adminService;
    private TeacherService teacherService;
    private CourseService courseService;
    private LectureService lectureService;
    private RoomService roomService;

    public AdminController(GroupService groupService, StudentService studentService, AdminService adminService,
            TeacherService teacherService, CourseService courseService, LectureService lectureService,
            RoomService roomService) {
        this.groupService = groupService;
        this.studentService = studentService;
        this.adminService = adminService;
        this.teacherService = teacherService;
        this.courseService = courseService;
        this.lectureService = lectureService;
        this.roomService = roomService;
    }

    @GetMapping("tables/groups")
    public String getGroups(@RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size, Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Group> groups = groupService.findAll(pageable);

        model.addAttribute("groups", groups);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", groups.getTotalPages());

        return "tables/groups";
    }

    @GetMapping("tables/students")
    public String getStudents(@RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size, Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Student> students = studentService.findAll(pageable);

        model.addAttribute("students", students);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", students.hasContent() ? students.getTotalPages() : 1);

        return "tables/students";
    }

    @GetMapping("tables/admins")
    public String getAdmins(@RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size, Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Admin> admins = adminService.findAll(pageable);

        model.addAttribute("admins", admins);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", admins.hasContent() ? admins.getTotalPages() : 1);

        return "tables/admins";
    }

    @GetMapping("tables/teachers")
    public String getTeachers(@RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size, Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Teacher> teachers = teacherService.findAll(pageable);

        model.addAttribute("teachers", teachers);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", teachers.hasContent() ? teachers.getTotalPages() : 1);

        return "tables/teachers";
    }

    @GetMapping("tables/courses")
    public String getCourses(@RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size, Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Course> courses = courseService.findAll(pageable);

        model.addAttribute("courses", courses);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", courses.hasContent() ? courses.getTotalPages() : 1);

        return "tables/courses";
    }

    @GetMapping("tables/lectures")
    public String getLectures(@RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size, Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Lecture> lectures = lectureService.findAll(pageable);

        model.addAttribute("lectures", lectures);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", lectures.hasContent() ? lectures.getTotalPages() : 1);

        return "tables/lectures";
    }

    @GetMapping("tables/rooms")
    public String getRooms(@RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size, Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Room> rooms = roomService.findAll(pageable);

        model.addAttribute("rooms", rooms);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", rooms.hasContent() ? rooms.getTotalPages() : 1);

        return "tables/rooms";
    }
}
