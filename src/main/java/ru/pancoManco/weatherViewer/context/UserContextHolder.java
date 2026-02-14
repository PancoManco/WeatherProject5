package ru.pancoManco.weatherViewer.context;

import ru.pancoManco.weatherViewer.model.Session;
import ru.pancoManco.weatherViewer.model.User;

import java.util.NoSuchElementException;
import java.util.UUID;

public class UserContextHolder {

    private final static ThreadLocal<User> context = new ThreadLocal<>();

    private UserContextHolder() {}

    public static void set(User user) {
        context.set(user);
    }
    public static User get() {
        User user = context.get();
//        if (user == null) {
//            throw new NoSuchElementException();
//        }
        return user;
    }
    public static void clear() {
        context.remove();
    }
}
