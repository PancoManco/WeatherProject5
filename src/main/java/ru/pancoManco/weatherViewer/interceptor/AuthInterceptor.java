package ru.pancoManco.weatherViewer.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import ru.pancoManco.weatherViewer.context.UserContextHolder;
import ru.pancoManco.weatherViewer.model.AuthUser;
import ru.pancoManco.weatherViewer.model.Session;
import ru.pancoManco.weatherViewer.model.User;
import ru.pancoManco.weatherViewer.service.SessionService;
import ru.pancoManco.weatherViewer.util.WebUtils;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final SessionService sessionService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        AuthUser authUser = null;
        Cookie cookie = WebUtils.findCookie(request, "SESSION_ID");
        if (cookie != null) {
            String sessionId = cookie.getValue();
            Optional<Session> sessionOpt = sessionService.getSessionById(UUID.fromString(sessionId));
            if (sessionService.isSessionValid(UUID.fromString(sessionId))) {
                if (sessionOpt.isPresent()) {
                    Session currentSession = sessionOpt.get();
                    User user = currentSession.getUserId();
                    authUser = new AuthUser(user.getId(), user.getLogin());
                    UserContextHolder.set(authUser);
                }
            }
        }
        String uri = request.getRequestURI();
        if (uri.startsWith("/css") || uri.startsWith("/js")||uri.startsWith("/images")) {
            return true;
        }
        if (authUser != null && (uri.equals("/sign-in") || uri.equals("/sign-up"))) {
            response.sendRedirect("/");
            return false;
        }

        if (authUser == null && !uri.equals("/sign-in") && !uri.equals("/sign-up")) {
            response.sendRedirect("/sign-in");
            return false;
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception e) {
        UserContextHolder.clear();
    }
}