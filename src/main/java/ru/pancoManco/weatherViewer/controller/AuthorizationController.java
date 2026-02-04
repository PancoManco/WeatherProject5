package ru.pancoManco.weatherViewer.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
                                    BindingResult bindingResult,
                                    Model model) {

        if (bindingResult.hasErrors()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                if (fieldError.getField().equals("repeatPassword")) {
                    model.addAttribute("passwordMismatch", true);
                }
            }
            return "sign-up";
        }
        userService.register(userRegisterDto);
        return "redirect:/sign-in";
    }

    // ===== LOGIN =====
    @GetMapping("/sign-in")
    public String getLoginPage(Model model) {
        model.addAttribute("newUser", new UserSignInDto());
        return "sign-in";
    }

    @PostMapping("/sign-in")
    public String signIn(  @ModelAttribute("newUser")  @Valid UserSignInDto userSignInDto,
                         BindingResult bindingResult,
                         Model model,
                         HttpSession session) {

        if (bindingResult.hasErrors()) {
            return "sign-in";
        }
        boolean authenticated = userService.authenticate(userSignInDto.getUsername(), userSignInDto.getPassword());

        if (!authenticated) {
            model.addAttribute("error", "Invalid username or password!");
            return "sign-in";
        }
        session.setAttribute("username", userSignInDto.getUsername());
     //   model.addAttribute("user", userSignInDto);
        return "redirect:/index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/sign-in";
    }

}
