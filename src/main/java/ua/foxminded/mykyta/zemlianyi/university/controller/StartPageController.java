package ua.foxminded.mykyta.zemlianyi.university.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StartPageController {

    @GetMapping("/")
    public String homePage() {
        return "index";
    }
}
