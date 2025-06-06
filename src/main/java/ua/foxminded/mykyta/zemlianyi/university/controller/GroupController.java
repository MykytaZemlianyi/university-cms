package ua.foxminded.mykyta.zemlianyi.university.controller;

import java.util.Collections;

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
import ua.foxminded.mykyta.zemlianyi.university.dto.Course;
import ua.foxminded.mykyta.zemlianyi.university.dto.Group;
import ua.foxminded.mykyta.zemlianyi.university.dto.Student;
import ua.foxminded.mykyta.zemlianyi.university.service.CourseService;
import ua.foxminded.mykyta.zemlianyi.university.service.GroupService;
import ua.foxminded.mykyta.zemlianyi.university.service.StudentService;

@Controller
@RequestMapping("/groups")
public class GroupController {
    private GroupService groupService;
    private StudentService studentService;
    private CourseService courseService;

    public GroupController(GroupService groupService, StudentService studentService, CourseService courseService) {
        this.groupService = groupService;
        this.studentService = studentService;
        this.courseService = courseService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_STUDENT','ROLE_TEACHER')")
    public String getGroups(@RequestParam(defaultValue = "0") Integer currentPage,
            @RequestParam(defaultValue = "5") Integer size, Model model) {
        Pageable pageable = PageRequest.of(currentPage, size);
        Page<Group> groups = groupService.findAll(pageable);

        model.addAttribute("groups", groups);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", groups.getTotalPages());

        return "view-all-groups";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String showCreateGroupForm(@RequestParam(defaultValue = "0") Integer studentPage,
            @RequestParam(defaultValue = "5") Integer studentPageSize,
            @RequestParam(defaultValue = "0") Integer coursePage,
            @RequestParam(defaultValue = "5") Integer coursePageSize, Model model) {

        prepareGroupFormModel(model, PageRequest.of(studentPage, studentPageSize),
                PageRequest.of(coursePage, coursePageSize));
        model.addAttribute("group", new Group());

        return "add-new-group";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String createGroup(@Valid @ModelAttribute Group group, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes, @RequestParam(defaultValue = "0") Integer studentPage,
            @RequestParam(defaultValue = "5") Integer studentPageSize,
            @RequestParam(defaultValue = "0") Integer coursePage,
            @RequestParam(defaultValue = "5") Integer coursePageSize) {

        if (bindingResult.hasErrors()) {
            prepareGroupFormModel(model, PageRequest.of(studentPage, studentPageSize),
                    PageRequest.of(coursePage, coursePageSize));
            return "add-new-group";
        }
        groupService.addNew(group);
        redirectAttributes.addFlashAttribute("successMessage", "Group added successfully!");

        return "redirect:/groups";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String showEditGroupForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes,
            @RequestParam(defaultValue = "0") Integer studentPage,
            @RequestParam(defaultValue = "5") Integer studentPageSize,
            @RequestParam(defaultValue = "0") Integer coursePage,
            @RequestParam(defaultValue = "5") Integer coursePageSize) {

        Group group = groupService.getByIdOrThrow(id);
        model.addAttribute("group", group);

        prepareGroupFormModel(model, PageRequest.of(studentPage, studentPageSize),
                PageRequest.of(coursePage, coursePageSize));
        return "edit-group";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String updateGroup(@PathVariable Long id, @Valid @ModelAttribute("group") Group updatedGroup,
            BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model,
            @RequestParam(defaultValue = "0") Integer studentPage,
            @RequestParam(defaultValue = "5") Integer studentPageSize,
            @RequestParam(defaultValue = "0") Integer coursePage,
            @RequestParam(defaultValue = "5") Integer coursePageSize) {
        addSelectedIdsToModel(model);
        if (bindingResult.hasErrors()) {
            prepareGroupFormModel(model, PageRequest.of(studentPage, studentPageSize),
                    PageRequest.of(coursePage, coursePageSize));
            return "edit-group";
        }

        groupService.update(updatedGroup);
        redirectAttributes.addFlashAttribute("successMessage", "Group updated successfully!");
        return "redirect:/groups";
    }

    private void prepareGroupFormModel(Model model, Pageable studentPageable, Pageable coursePageable) {
        addSelectedIdsToModel(model);

        Page<Student> studentPageObj = studentService.findAll(studentPageable);
        model.addAttribute("studentPage", studentPageObj);

        Page<Course> coursePageObj = courseService.findAll(coursePageable);
        model.addAttribute("coursePage", coursePageObj);

    }

    private void addSelectedIdsToModel(Model model) {
        model.addAttribute("selectedStudentIds", Collections.emptySet());
        model.addAttribute("selectedCourseIds", Collections.emptySet());
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String deleteGroup(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        groupService.deleteOrThrow(id);
        redirectAttributes.addFlashAttribute("successMessage", "Group deleted successfully!");

        return "redirect:/groups";
    }

    @PostMapping("/groupSelectRadioList")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String getGroupRadioList(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size, @RequestParam(required = false) Long selectedGroupId,
            Model model) {
        Page<Group> groupPage = groupService.findAll(PageRequest.of(page, size));
        model.addAttribute("groupPage", groupPage);
        model.addAttribute("selectedGroupId", selectedGroupId);
        return "fragments/group_fragments :: groupSelectRadioList";
    }

}
