package ru.pancoManco.weatherViewer.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.pancoManco.weatherViewer.dto.UserSignInDto;
import ru.pancoManco.weatherViewer.service.UserService;
import ru.pancoManco.weatherViewer.util.WebUtils;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sign-in")
public class SignInController {
    private final UserService userService;

    @GetMapping
    public String getLoginPage(@ModelAttribute("newUser") UserSignInDto userSignInDto) {
        return "sign-in";
    }

    @PostMapping
    public String signIn(@ModelAttribute("newUser")  @Valid UserSignInDto userSignInDto,
                         BindingResult bindingResult, HttpServletResponse resp,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "sign-in";
        }
        if (!userService.isCorrectPasswordOrLogin(userSignInDto)) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Invalid username or password");
            return "redirect:/sign-in";
        }

        UUID sessionId = userService.authenticate(userSignInDto);
        WebUtils.setCookie("SESSION_ID",sessionId.toString(), 60 * 60,resp);
        return "redirect:/";
    }
}
