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
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SessionService sessionService;
    private final UserMapper userMapper;

    @Transactional
    public void register(UserRegisterDto userRegisterDto) {
        User user = userMapper.toEntity(userRegisterDto);
        String encodedPassword = PasswordEncoderUtil.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }
    @Transactional
    public UUID authenticate(UserSignInDto userSignInDto)  {
       Optional<User> opt = userRepository.findByUsername(userSignInDto.getUsername());
        return sessionService.createNewSession(opt.get());
    }
    @Transactional(readOnly = true)
    public boolean isAlreadyExist(UserRegisterDto userRegisterDto) {
        return userRepository.findByUsername(userRegisterDto.getUsername()).isPresent();
    }
    @Transactional(readOnly = true)
    public boolean isCorrectPasswordOrLogin(UserSignInDto userSignInDto) {
        return userRepository.findByUsername(userSignInDto.getUsername())
                .map(user -> PasswordEncoderUtil.matches(userSignInDto.getPassword(), user.getPassword()))
                .orElse(false);
    }
    @Transactional(readOnly = true)
    @Cacheable(value = "usersByUsername",key = "#username", condition = "#username != null")
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}
