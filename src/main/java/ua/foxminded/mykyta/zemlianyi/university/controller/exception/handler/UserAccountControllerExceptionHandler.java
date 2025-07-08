package ua.foxminded.mykyta.zemlianyi.university.controller.exception.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ua.foxminded.mykyta.zemlianyi.university.controller.UserAccountController;

@ControllerAdvice(assignableTypes = UserAccountController.class)
public class UserAccountControllerExceptionHandler {
    private static Logger logger = LogManager.getLogger(UserAccountControllerExceptionHandler.class.getName());

    @ExceptionHandler(Exception.class)
    public String handleAccountErrors(Exception ex, RedirectAttributes redirectAttributes) throws Exception {
        logger.warn(ex.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", "Error: " + ex.getMessage());
        return "redirect:/account";
    }

}
