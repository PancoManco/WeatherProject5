package ru.pancoManco.weatherViewer.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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
    public String getRegistrationPage(@ModelAttribute("newUser") UserRegisterDto userRegisterDto) {
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String createUserAccount(@ModelAttribute("newUser") @Valid  UserRegisterDto userRegisterDto,
                                    BindingResult bindingResult)
    {
        if (bindingResult.hasErrors()) {
            return "sign-up-with-errors";
        }
        userService.register(userRegisterDto);
        return "redirect:/sign-in";
    }

    // ===== LOGIN =====
    @GetMapping("/sign-in")
    public String getLoginPage(@ModelAttribute("newUser") UserSignInDto userSignInDto) {
        return "sign-in";
    }

    @PostMapping("/sign-in")
    public String signIn(  @ModelAttribute("newUser")  @Valid UserSignInDto userSignInDto,
                         BindingResult bindingResult,
                         HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "sign-in-with-errors";
        }
        boolean authenticated = userService.authenticate(userSignInDto);

if (authenticated) {
    return "index";
}
        return "redirect:/sign-in";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/sign-in";
    }

}
