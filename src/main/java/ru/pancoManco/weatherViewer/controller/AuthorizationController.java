package ru.pancoManco.weatherViewer.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.pancoManco.weatherViewer.model.User;
import ru.pancoManco.weatherViewer.service.UserService;

@Controller
public class AuthorizationController {
    private final UserService userService;
    @Autowired
    public AuthorizationController(UserService userService) {
        this.userService = userService;
    }


    // ===== REGISTRATION =====
    @GetMapping("/sign-up")
    public String getRegistrationPage(Model model) {
        model.addAttribute("user", new User());
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String createUserAccount(@ModelAttribute("user") User user) {
        userService.register(user);
        return "redirect:/sign-in";
    }

    // ===== LOGIN =====
    @GetMapping("/sign-in")
    public String getLoginPage(Model model) {
        model.addAttribute("user", new User());
        return "sign-in";
    }

    @PostMapping("/sign-in")
    public String signIn(@ModelAttribute("user") User user, Model model, HttpSession session) {

        boolean authenticated = userService.authenticate(user.getLogin(), user.getPassword());
        if (!authenticated) {
            model.addAttribute("error", "Invalid username or password!");
            return "sign-in";
        }
        session.setAttribute("username", user.getLogin());
        model.addAttribute("user", user);
        return "redirect:/index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/sign-in";
    }

}
