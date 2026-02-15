package ru.pancoManco.weatherViewer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pancoManco.weatherViewer.dto.UserRegisterDto;
import ru.pancoManco.weatherViewer.dto.UserSignInDto;
import ru.pancoManco.weatherViewer.mapper.UserMapper;
import ru.pancoManco.weatherViewer.model.User;
import ru.pancoManco.weatherViewer.repository.UserRepository;
import ru.pancoManco.weatherViewer.util.PasswordEncoderUtil;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SessionService sessionService;
    private final PasswordEncoderUtil passwordEncoder;
    private final UserMapper userMapper;


    public void register(UserRegisterDto userRegisterDto) {
        User user = userMapper.toEntity(userRegisterDto);
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    public UUID authenticate(UserSignInDto userSignInDto)  {
       Optional<User> opt = userRepository.findByUsername(userSignInDto.getUsername());
        return sessionService.createNewSession(opt.get());
    }

    public boolean isAlreadyExist(UserRegisterDto userRegisterDto) {
        return userRepository.findByUsername(userRegisterDto.getUsername()).isPresent();
    }

    public boolean isCorrectPasswordOrLogin(UserSignInDto userSignInDto) {
        return userRepository.findByUsername(userSignInDto.getUsername())
                .map(user -> passwordEncoder.matches(userSignInDto.getPassword(), user.getPassword()))
                .orElse(false);
    }

    @Cacheable(value = "usersByUsername",key = "#username", condition = "#username != null")
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}
