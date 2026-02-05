package ru.pancoManco.weatherViewer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pancoManco.weatherViewer.dto.UserRegisterDto;
import ru.pancoManco.weatherViewer.exception.UserAlreadyExist;
import ru.pancoManco.weatherViewer.mapper.UserMapper;
import ru.pancoManco.weatherViewer.model.User;
import ru.pancoManco.weatherViewer.repository.UserRepository;
import ru.pancoManco.weatherViewer.util.PasswordEncoderUtil;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoderUtil passwordEncoder;
    private final UserMapper userMapper;


    public void register(UserRegisterDto userRegisterDto) {
        User user = userMapper.toEntity(userRegisterDto);
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        if (userRepository.findByUsername(user.getLogin()) != null) {
            throw new UserAlreadyExist("User already exist");
        }
        userRepository.save(user);
    }
    public boolean authenticate(String login,String rawPassword) {
        User user = userRepository.findByUsername(login);
        if (user == null) {
            return false;
        }
        return passwordEncoder.matches(rawPassword,user.getPassword());
    }

}
