package ua.foxminded.mykyta.zemlianyi.university.controller.exception.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ua.foxminded.mykyta.zemlianyi.university.controller.TeacherController;

@ControllerAdvice(assignableTypes = TeacherController.class)
public class TeacherControllerExceptionHandler {
    private static Logger logger = LogManager.getLogger(TeacherControllerExceptionHandler.class.getName());

    @ExceptionHandler(Exception.class)
    public String handleTeacherErrors(Exception ex, RedirectAttributes redirectAttributes) {
        logger.warn(ex.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", "Error: " + ex.getMessage());
        return "redirect:/teachers";
    }

}
