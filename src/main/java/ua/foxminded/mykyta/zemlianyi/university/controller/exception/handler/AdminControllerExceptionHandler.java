package ua.foxminded.mykyta.zemlianyi.university.controller.exception.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ua.foxminded.mykyta.zemlianyi.university.controller.AdminController;

@ControllerAdvice(assignableTypes = AdminController.class)
public class AdminControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleAdminNotFound(Exception ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "Error: " + ex.getMessage());
        return "redirect:/admins";
    }

}
