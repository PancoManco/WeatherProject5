package ru.pancoManco.weatherViewer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pancoManco.weatherViewer.model.User;
import ru.pancoManco.weatherViewer.repository.UserRepository;
import ru.pancoManco.weatherViewer.util.BCryptPasswordEncoder;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register(User user) {
        if (userRepository.findByUsername(user.getLogin()) != null) {
            throw new RuntimeException("User already exists");
        }
        String encodedPassword = BCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }
    public boolean authenticate(String login,String rawPassword) {
        User user = userRepository.findByUsername(login);
        if (user == null) {
            return false;
        }
        return BCryptPasswordEncoder.matches(rawPassword,user.getPassword());
    }

}
