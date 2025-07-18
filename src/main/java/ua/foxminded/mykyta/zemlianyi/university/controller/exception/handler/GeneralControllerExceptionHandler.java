package ua.foxminded.mykyta.zemlianyi.university.controller.exception.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@ControllerAdvice
public class GeneralControllerExceptionHandler {
    private static Logger logger = LogManager.getLogger(GeneralControllerExceptionHandler.class.getName());

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(Exception ex, RedirectAttributes redirectAttributes) {
        logger.warn(ex.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", "Error: " + ex.getMessage());
        return "redirect:/welcome";
    }

}
