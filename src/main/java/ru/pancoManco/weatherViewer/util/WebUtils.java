package ru.pancoManco.weatherViewer.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public final class WebUtils {
    public static final String SESSION_COOKIE_NAME = "SESSION_ID";

    private WebUtils() {
    }
    public static Cookie findCookie(HttpServletRequest req, String cookieName) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals(cookieName)) {
                    if (c.getValue() != null && !"".equals(c.getValue())) {
                        return c;
                    }
                }
            }
        }
        return null;
    }
    public static void setCookie(String name, String value, int age, HttpServletResponse resp) {
        Cookie c = new Cookie(name, value);
        c.setMaxAge(age);
        c.setPath("/");
        c.setHttpOnly(true);
        resp.addCookie(c);
    }

    public static void deleteSessionCookie(HttpServletResponse response) {
        Cookie emptyCookie = new Cookie(SESSION_COOKIE_NAME, null);
        emptyCookie.setPath("/");
        emptyCookie.setMaxAge(0);
        response.addCookie(emptyCookie);
    }
}
