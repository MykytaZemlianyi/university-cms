package ua.foxminded.mykyta.zemlianyi.university.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ua.foxminded.mykyta.zemlianyi.university.Constants;
import ua.foxminded.mykyta.zemlianyi.university.dto.User;
import ua.foxminded.mykyta.zemlianyi.university.service.AdminService;
import ua.foxminded.mykyta.zemlianyi.university.service.StaffService;
import ua.foxminded.mykyta.zemlianyi.university.service.StudentService;
import ua.foxminded.mykyta.zemlianyi.university.service.TeacherService;
import ua.foxminded.mykyta.zemlianyi.university.service.UserService;
import ua.foxminded.mykyta.zemlianyi.university.service.UserServiceResolver;

@Controller
public class GeneralController {
    private UserServiceResolver userServiceResolver;

    public GeneralController(UserServiceResolver userServiceResolver) {
        this.userServiceResolver = userServiceResolver;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/welcome")
    public String welcomePage() {
        return "welcome";
    }

    @GetMapping("/account")
    public String myProfilePage(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().findFirst()
                .map(Object::toString).orElse("").substring(5);

        UserService<?> service = userServiceResolver.resolveUserService(role);
        User user = service.getByEmailOrThrow(username);

        model.addAttribute("user", user);
        return "view-my-account";
    }

    @PostMapping("/account/change-password")
    public String changePassword(@RequestParam String currentPassword, @RequestParam String newPassword,
            RedirectAttributes redirectAttributes) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().findFirst()
                .map(Object::toString).orElse("").substring(5);

        UserService<?> service = userServiceResolver.resolveUserService(role);
        service.changePassword(username, currentPassword, newPassword);
        redirectAttributes.addFlashAttribute("successMessage", "Password changed successfully!");
        return "redirect:/account";
    }

}
