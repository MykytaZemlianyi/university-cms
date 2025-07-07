package ua.foxminded.mykyta.zemlianyi.university.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ua.foxminded.mykyta.zemlianyi.university.dto.User;
import ua.foxminded.mykyta.zemlianyi.university.service.UserServiceResolver;

@Controller
public class UserAccountController {
    private UserServiceResolver userServiceResolver;

    public UserAccountController(UserServiceResolver userServiceResolver) {
        this.userServiceResolver = userServiceResolver;
    }

    @GetMapping("/account")
    public String myAccount(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().findFirst()
                .map(Object::toString).orElse("").substring(5);

        User user = userServiceResolver.getUserByEmailAndRole(username, role);

        model.addAttribute("user", user);
        return "view-my-account";
    }

    @PostMapping("/account/change-password")
    public String changePassword(@RequestParam String currentPassword, @RequestParam String newPassword,
            RedirectAttributes redirectAttributes) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().findFirst()
                .map(Object::toString).orElse("").substring(5);

        userServiceResolver.changePasswordForUserByEmailAndRole(username, role, currentPassword, newPassword);
        redirectAttributes.addFlashAttribute("successMessage", "Password changed successfully!");
        return "redirect:/account";
    }

}
