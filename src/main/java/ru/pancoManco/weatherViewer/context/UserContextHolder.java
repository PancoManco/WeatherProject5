package ru.pancoManco.weatherViewer.context;

import ru.pancoManco.weatherViewer.model.AuthUser;


public class UserContextHolder {

    private final static ThreadLocal<AuthUser> context = new ThreadLocal<>();
    private UserContextHolder() {}
    public static void set(AuthUser authUser) {
        context.set(authUser);
    }
    public static AuthUser get() {
        return context.get();
    }
    public static void clear() {
        context.remove();
    }
}
