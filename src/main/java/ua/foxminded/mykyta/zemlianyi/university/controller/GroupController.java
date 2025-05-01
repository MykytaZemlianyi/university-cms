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
public class GroupController {
    private GroupService groupService;
    private StudentService studentService;
    private CourseService courseService;

    public GroupController(GroupService groupService, StudentService studentService, CourseService courseService) {
        this.groupService = groupService;
        this.studentService = studentService;
        this.courseService = courseService;
    }

    @GetMapping("/admin/groups")
    public String getGroups(@RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size, Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Group> groups = groupService.findAll(pageable);

        model.addAttribute("groups", groups);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", groups.getTotalPages());

        return "view-all-groups";
    }

    @GetMapping("/admin/add-new-group")
    public String showCreateGroupForm(Model model) {
        List<Student> allStudents = studentService.findAll();
        List<Course> allCourses = courseService.findAll();
        model.addAttribute("group", new Group());
        model.addAttribute("studentList", allStudents);
        model.addAttribute("courseList", allCourses);
        return "add-new-group";
    }

    @PostMapping("/admin/add-group")
    public String createGroup(@Valid @ModelAttribute Group group, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "add-new-group";
        }

        try {
            groupService.addNew(group);
            redirectAttributes.addFlashAttribute("successMessage", "Group added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/admin/groups";
    }

    @GetMapping("/admin/edit-group/{id}")
    public String showEditGroupForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Group> groupOpt = groupService.findById(id);
        List<Student> allStudents = studentService.findAll();
        List<Course> allCourses = courseService.findAll();
        if (groupOpt.isPresent()) {
            model.addAttribute("group", groupOpt.get());
            model.addAttribute("studentList", allStudents);
            model.addAttribute("courseList", allCourses);
            return "edit-group";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error: " + Constants.OBJECT_UPDATE_FAIL_DOES_NOT_EXIST);
            return "redirect:/admin/groups";
        }
    }

    @PostMapping("/admin/edit-group/{id}")
    public String updateGroup(@PathVariable Long id, @Valid @ModelAttribute("group") Group updatedGroup,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "edit-group";
        }

        try {
            groupService.update(updatedGroup);
            redirectAttributes.addFlashAttribute("successMessage", "Group updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/admin/groups";
    }

    @DeleteMapping("/admin/delete-group/{id}")
    public String deleteGroup(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Group> group = groupService.findById(id);
            if (group.isPresent()) {
                groupService.delete(group.get());
                redirectAttributes.addFlashAttribute("successMessage", "Group deleted successfully!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Error: Group does not exists");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/admin/groups";
    }
}
