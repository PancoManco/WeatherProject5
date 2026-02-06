package ru.pancoManco.weatherViewer.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public final class CookieUtil {
    public static final String SESSION_COOKIE_NAME = "SESSION_ID";
    private CookieUtil() {}

//    public static Optional<UUID> extractSessionId(HttpServletRequest request) {
//        if (request.getCookies() != null) {
//            return Optional.empty();
//        }
//        return Arrays.stream(request.getCookies())
//                .filter(c -> SESSION_COOKIE_NAME.equals(c.getName()))
//                .map(Cookie::getValue)
//                .map(UUID::fromString)
//                .findFirst();
//    }

    public static void deleteSessionCookie(HttpServletResponse response) {
        Cookie emptyCookie = new Cookie(SESSION_COOKIE_NAME, null);
        emptyCookie.setPath("/");
        emptyCookie.setMaxAge(0);
        response.addCookie(emptyCookie);
    }
}
