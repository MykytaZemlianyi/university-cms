package ua.foxminded.mykyta.zemlianyi.university.controller.exception.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ua.foxminded.mykyta.zemlianyi.university.controller.StudentController;

@ControllerAdvice(assignableTypes = StudentController.class)
public class StudentControllerExceptionHandler {
    private static Logger logger = LogManager.getLogger(StudentControllerExceptionHandler.class.getName());

    @ExceptionHandler(Exception.class)
    public String handleStudentErrors(Exception ex, RedirectAttributes redirectAttributes) {
        logger.warn(ex.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", "Error: " + ex.getMessage());
        return "redirect:/students";
    }

}
