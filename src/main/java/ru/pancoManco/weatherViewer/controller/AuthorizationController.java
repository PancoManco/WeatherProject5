package ru.pancoManco.weatherViewer.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.pancoManco.weatherViewer.dto.UserRegisterDto;
import ru.pancoManco.weatherViewer.dto.UserSignInDto;
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
        model.addAttribute("user", new UserRegisterDto());
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String createUserAccount(@ModelAttribute("user") UserRegisterDto userRegisterDto) {
        userService.register(userRegisterDto);
        return "redirect:/sign-in";
    }

    // ===== LOGIN =====
    @GetMapping("/sign-in")
    public String getLoginPage(Model model) {
        model.addAttribute("user", new UserSignInDto());
        return "sign-in";
    }

    @PostMapping("/sign-in")
    public String signIn(@ModelAttribute("user") UserSignInDto userSignInDto, Model model, HttpSession session) {

        boolean authenticated = userService.authenticate(userSignInDto.getUsername(), userSignInDto.getPassword());
        if (!authenticated) {
            model.addAttribute("error", "Invalid username or password!");
            return "sign-in";
        }
        session.setAttribute("username", userSignInDto.getUsername());
        model.addAttribute("user", userSignInDto);
        return "redirect:/index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/sign-in";
    }

}
