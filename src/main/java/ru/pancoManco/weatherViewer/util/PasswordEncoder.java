package ru.pancoManco.weatherViewer.util;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder {

    public static String encode(String rawPassword) {
            return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
        }
    public static boolean matches(String rawPassword, String encodedPassword) {
        return BCrypt.checkpw(rawPassword, encodedPassword);
    }

}
