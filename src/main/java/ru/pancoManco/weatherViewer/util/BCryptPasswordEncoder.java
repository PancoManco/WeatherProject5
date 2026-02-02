package ru.pancoManco.weatherViewer.util;

import org.springframework.security.crypto.bcrypt.BCrypt;


public final class BCryptPasswordEncoder {
    private BCryptPasswordEncoder() {
    }

    public static String encode(String rawPassword) {
            return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
        }
    public boolean matches(String rawPassword, String encodedPassword) {
        return BCrypt.checkpw(rawPassword, encodedPassword);
    }

}
