package ua.foxminded.mykyta.zemlianyi.university.controller;

import java.net.BindException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static Logger logger = LogManager.getLogger(GlobalExceptionHandler.class.getName());

    @ExceptionHandler(BindException.class)
    public String handleError(HttpServletRequest req, BindException e, RedirectAttributes redirectAttributes) {

        String errorMessage = ExceptionUtils.getRootCauseMessage(e) != null ? ExceptionUtils.getRootCauseMessage(e)
                : "Unexpected error occurred.";

        logger.error("Request: {} raised {}", req.getRequestURL(), e);
        redirectAttributes.addFlashAttribute("errorMessage", "Error: " + errorMessage);
        return "redirect:" + req.getHeader("Referer");
    }
}
