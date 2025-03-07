package ua.foxminded.mykyta.zemlianyi.university.controller;

import java.util.LinkedHashMap;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ua.foxminded.mykyta.zemlianyi.university.dto.Group;
import ua.foxminded.mykyta.zemlianyi.university.dto.Student;
import ua.foxminded.mykyta.zemlianyi.university.service.GroupService;
import ua.foxminded.mykyta.zemlianyi.university.service.StudentService;

@Controller
public class AdminController {
    private GroupService groupService;
    private StudentService studentService;

    public AdminController(GroupService groupService, StudentService studentService) {
        this.groupService = groupService;
        this.studentService = studentService;
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
        if (students == null) {
            students = Page.empty(pageable);
        }

        LinkedHashMap<String, Function<Student, Object>> columnData = new LinkedHashMap<>();
        columnData.put("ID", Student::getId);
        columnData.put("Name", Student::getName);
        columnData.put("Surname", Student::getSurname);
        columnData.put("Group", student -> student.getGroup() != null ? student.getGroup().getName() : "No group");

        model.addAttribute("students", students);
        model.addAttribute("columns", columnData);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", students.hasContent() ? students.getTotalPages() : 1);

        return "admin/students";
    }

}
