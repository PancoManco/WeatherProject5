package ru.pancoManco.weatherViewer.context;

import ru.pancoManco.weatherViewer.model.User;



public class UserContextHolder {

    private final static ThreadLocal<User> context = new ThreadLocal<>();

    private UserContextHolder() {}

    public static void set(User user) {
        context.set(user);
    }
    public static User get() {
        return context.get();
    }
    public static void clear() {
        context.remove();
    }
}
