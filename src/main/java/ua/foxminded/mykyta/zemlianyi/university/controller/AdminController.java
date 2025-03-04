package ua.foxminded.mykyta.zemlianyi.university.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin/groups")
    public String getGroups(Model model, Authentication authentication) {
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        if (role.startsWith("ROLE_")) {
            role = role.substring(5);
        }

        model.addAttribute("role", role);

        model.addAttribute("text", "Groups for Admin");
        return "admin/groups";
    }

}
