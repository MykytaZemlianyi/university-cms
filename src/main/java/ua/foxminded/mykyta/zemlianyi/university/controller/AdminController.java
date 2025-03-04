package ua.foxminded.mykyta.zemlianyi.university.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ua.foxminded.mykyta.zemlianyi.university.dto.Group;
import ua.foxminded.mykyta.zemlianyi.university.service.GroupService;

@Controller
public class AdminController {
    private GroupService groupService;

    public AdminController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/admin/groups")
    public String getGroups(Model model) {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Group> groups = groupService.findAll(pageable);
        model.addAttribute("groups", groups);
        return "admin/groups";
    }

}
