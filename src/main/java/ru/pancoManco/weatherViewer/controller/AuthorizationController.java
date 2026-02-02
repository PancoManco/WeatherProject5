package ru.pancoManco.weatherViewer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.pancoManco.weatherViewer.model.User;
import ru.pancoManco.weatherViewer.service.UserService;
import ru.pancoManco.weatherViewer.util.BCryptPasswordEncoder;

@Controller
public class AuthorizationController {
    private final UserService userService;


    @Autowired
    public AuthorizationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/sign-in")
    public String getLoginPage() {
        return "sign-in";
    }

    @GetMapping("/sign-up")
    public String getRegistrationPage() {
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String createUserAccount(@ModelAttribute User user) {
        String encodedPassword = BCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userService.save(user);
        return "redirect:/sign-in";
    }
}
