package ru.pancoManco.weatherViewer.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        HttpSession session = request.getSession(false);
        String uri = request.getRequestURI();
        if (uri.equals("/sign-in") || uri.equals("/sign-up")) {
            return true;
        }
        if (session != null && session.getAttribute("username") != null) {
            return true;
        }
        response.sendRedirect("/sign-in");
        return false;
    }
}
