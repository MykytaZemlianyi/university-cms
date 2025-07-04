package ua.foxminded.mykyta.zemlianyi.university.controller;

import java.util.Collections;
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
import ua.foxminded.mykyta.zemlianyi.university.dto.Group;
import ua.foxminded.mykyta.zemlianyi.university.dto.Student;
import ua.foxminded.mykyta.zemlianyi.university.service.GroupService;
import ua.foxminded.mykyta.zemlianyi.university.service.StudentService;

@Controller
@RequestMapping("/students")
public class StudentController {
    private StudentService studentService;
    private GroupService groupService;

    public StudentController(StudentService studentService, GroupService groupService) {
        this.studentService = studentService;
        this.groupService = groupService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_TEACHER','ROLE_STAFF')")
    public String getStudents(@RequestParam(defaultValue = "0") Integer currentPage,
            @RequestParam(defaultValue = "5") Integer size, Model model) {

        Pageable pageable = PageRequest.of(currentPage, size);
        Page<Student> students = studentService.findAll(pageable);

        model.addAttribute("students", students);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", students.hasContent() ? students.getTotalPages() : 1);

        return "view-all-students";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String showCreateStudentForm(Model model, @RequestParam(defaultValue = "0") Integer groupPage,
            @RequestParam(defaultValue = "5") Integer groupPageSize) {

        prepareModelForStudentForm(model, new Student(), PageRequest.of(groupPage, groupPageSize));
        return "add-new-student";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String createStudent(@Valid @ModelAttribute Student student, BindingResult bindingResult,
            RedirectAttributes redirectAttributes, Model model, @RequestParam(defaultValue = "0") Integer groupPage,
            @RequestParam(defaultValue = "5") Integer groupSize) {

        if (bindingResult.hasErrors()) {
            prepareModelForStudentForm(model, student, PageRequest.of(groupPage, groupSize));
            return "add-new-student";
        }

        studentService.addNew(student);
        redirectAttributes.addFlashAttribute("successMessage", "Student added successfully!");
        return "redirect:/students";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public String showEditStudentForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes,
            @RequestParam(defaultValue = "0") Integer groupPage, @RequestParam(defaultValue = "5") Integer groupSize,
            @RequestParam(required = false) Long selectedGroupId) {
        Student student = studentService.getByIdOrThrow(id);
        prepareModelForStudentForm(model, student, PageRequest.of(groupPage, groupSize));
        return "edit-student";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public String updateStudent(@PathVariable Long id, @Valid @ModelAttribute("student") Student updatedStudent,
            BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model,
            @RequestParam(defaultValue = "0") Integer groupPage, @RequestParam(defaultValue = "5") Integer groupSize) {

        if (bindingResult.hasErrors()) {
            prepareModelForStudentForm(model, updatedStudent, PageRequest.of(groupPage, groupSize));
            return "edit-student";
        }

        studentService.update(updatedStudent);
        redirectAttributes.addFlashAttribute("successMessage", "Student updated successfully!");
        return "redirect:/students";
    }

    private void prepareModelForStudentForm(Model model, Student student, Pageable pageable) {
        Page<Group> groupPageObj = groupService.findAll(pageable);
        model.addAttribute("groupPage", groupPageObj);
        model.addAttribute("student", student);
        model.addAttribute("selectedGroupId", student.getGroup() != null ? student.getGroup().getId() : null);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String deleteStudent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        studentService.deleteOrThrow(id);
        redirectAttributes.addFlashAttribute("successMessage", "Student deleted successfully!");
        return "redirect:/students";
    }

    @PostMapping("/studentSelectCheckboxList")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STAFF')")
    public String getStudentSelectCheckboxList(@ModelAttribute Group group,
            @RequestParam(defaultValue = "0") Integer currentPage, @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(required = false) Set<Long> selectedStudentIds, Model model) {

        if (selectedStudentIds == null) {
            selectedStudentIds = Collections.emptySet();
        }

        Pageable pageable = PageRequest.of(currentPage, size);
        Page<Student> studentPage = studentService.findAll(pageable);

        model.addAttribute("studentPage", studentPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", studentPage.getTotalPages());
        model.addAttribute("selectedStudentIds", selectedStudentIds);
        model.addAttribute("group", group);

        return "fragments/student_fragments :: studentSelectCheckboxList";
    }
}
