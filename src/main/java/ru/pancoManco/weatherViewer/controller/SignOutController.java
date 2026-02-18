package ru.pancoManco.weatherViewer.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.pancoManco.weatherViewer.service.SessionService;
import ru.pancoManco.weatherViewer.util.WebUtils;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class SignOutController {
    private final SessionService sessionService;

    @GetMapping("/signOut")
    public String logout(HttpServletRequest req, HttpServletResponse resp) {
        Cookie cookie = WebUtils.findCookie(req, "SESSION_ID");
        sessionService.invalidateSession(UUID.fromString(cookie.getValue()));
        WebUtils.deleteSessionCookie(resp);
        return "redirect:/sign-in";
    }
}
