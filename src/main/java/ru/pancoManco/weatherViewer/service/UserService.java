package ru.pancoManco.weatherViewer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pancoManco.weatherViewer.dto.UserRegisterDto;
import ru.pancoManco.weatherViewer.mapper.UserMapper;
import ru.pancoManco.weatherViewer.model.User;
import ru.pancoManco.weatherViewer.repository.UserRepository;
import ru.pancoManco.weatherViewer.util.BCryptPasswordEncoder;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserMapper userMapper;


    public void register(UserRegisterDto userRegisterDto) {
        User user = userMapper.toEntity(userRegisterDto);
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        if (userRepository.findByUsername(user.getLogin()) != null) {
            throw new RuntimeException("User already exists");
        }
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
