package ru.pancoManco.weatherViewer.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.pancoManco.weatherViewer.dto.UserRegisterDto;
import ru.pancoManco.weatherViewer.service.UserService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sign-up")
public class SignUpController {

    private final UserService userService;

    @GetMapping
    public String getRegistrationPage(@ModelAttribute("newUser") UserRegisterDto userRegisterDto) {
        return "sign-up";
    }

    @PostMapping
    public String createUserAccount(@ModelAttribute("newUser") @Valid UserRegisterDto userRegisterDto,
                                    BindingResult bindingResult)
    {
        if (bindingResult.hasErrors()) {
            return "sign-up-with-errors";
        }
        userService.register(userRegisterDto);
        return "redirect:/sign-in";
    }
}
