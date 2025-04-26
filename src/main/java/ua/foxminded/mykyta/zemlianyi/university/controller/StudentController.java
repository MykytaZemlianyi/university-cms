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
import ua.foxminded.mykyta.zemlianyi.university.dto.Group;
import ua.foxminded.mykyta.zemlianyi.university.dto.Student;
import ua.foxminded.mykyta.zemlianyi.university.service.GroupService;
import ua.foxminded.mykyta.zemlianyi.university.service.StudentService;

@Controller
public class StudentController {
    private StudentService studentService;
    private GroupService groupService;

    public StudentController(StudentService studentService, GroupService groupService) {
        this.studentService = studentService;
        this.groupService = groupService;
    }

    @GetMapping("/admin/students")
    public String getStudents(@RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size, Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Student> students = studentService.findAll(pageable);

        model.addAttribute("students", students);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", students.hasContent() ? students.getTotalPages() : 1);

        return "view-all-students";
    }

    @GetMapping("/admin/add-new-student")
    public String showCreateStudentForm(Model model) {
        List<Group> allGroups = groupService.findAll();
        model.addAttribute("student", new Student());
        model.addAttribute("groups", allGroups);
        return "add-new-student";
    }

    @PostMapping("/admin/add-student")
    public String createStudent(@Valid @ModelAttribute Student student, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "add-new-student";
        }

        try {
            studentService.addNew(student);
            redirectAttributes.addFlashAttribute("successMessage", "Student added successfully!");
            return "redirect:/admin/students";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
            return "redirect:/admin/students";
        }
    }

    @GetMapping("/admin/edit-student/{id}")
    public String showEditStudentForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Student> student = studentService.findById(id);
        List<Group> allGroups = groupService.findAll();
        if (student.isPresent()) {
            model.addAttribute("student", student.get());
            model.addAttribute("groups", allGroups);
            return "edit-student";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + Constants.USER_NOT_FOUND_ERROR);
            return "redirect:/admin/students";
        }
    }

    @PostMapping("/admin/edit-student/{id}")
    public String updateStudent(@PathVariable Long id, @RequestParam(required = false) Long groupId,
            @Valid @ModelAttribute("student") Student updatedStudent, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "edit-teacher";
        }

        try {
            studentService.update(updatedStudent);
            redirectAttributes.addFlashAttribute("successMessage", "Student updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/admin/students";
    }

    @DeleteMapping("/admin/delete-student/{id}")
    public String deleteTeacher(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Student> student = studentService.findById(id);
            if (student.isPresent()) {
                studentService.delete(student.get());
                redirectAttributes.addFlashAttribute("successMessage", "Student deleted successfully!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Error: Student does not exists");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/admin/students";
    }
}
