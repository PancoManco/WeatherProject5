package ru.pancoManco.weatherViewer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthorizationController {

    @GetMapping("/sign-in")
    public String getLoginPage() {
        return "sign-in";
    }

    @GetMapping("/sign-up")
    public String getRegistrationPage() {
        return "sign-up";
    }
}
