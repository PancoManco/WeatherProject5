import org.flywaydb.core.Flyway;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.pancoManco.weatherViewer.dto.UserRegisterDto;
import ru.pancoManco.weatherViewer.model.User;
import ru.pancoManco.weatherViewer.service.UserService;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
public class RegistrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private Flyway flyway;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("Проверка регистрации пользователя и сохранения в БД")
    public void registerUser() {
        String username = "test"+Math.random();
        UserRegisterDto userRegisterDto =
                new UserRegisterDto(username,"password1","password1");
        userService.register(userRegisterDto);
        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM users WHERE username = ?",
                Long.class,
                userRegisterDto.getUsername()
        );
        assertEquals(1, count, "Пользователь должен быть сохранен в БД");
        User savedUser = userService.getUserByUsername(username);
        assertNotNull("Новый зарегистрированный пользователь найден", savedUser);
    }

    @Test
    @DisplayName("Проверка при регистрации с неуникальным логином")
    public void nonUniqueUsernameRegister() {
        String username = "test"+Math.random();
        UserRegisterDto userRegisterDto =
                new UserRegisterDto(username,"password1","password1");
        userService.register(userRegisterDto);
       boolean exists = userService.isAlreadyExist(userRegisterDto);
        assertTrue("Пользователь с данным именем уже зарегистрирован", exists);
    }

    @Test
    @DisplayName("Проверка на неправильность логина или пароля")
    public void checkingUsernameAndPassword() {
        String username = "test"+Math.random();
        UserRegisterDto userRegisterDto =
                new UserRegisterDto(username,"password1","password1");
        userService.register(userRegisterDto);
        boolean wrongLoginOrPassword = userService.isAlreadyExist(userRegisterDto);
        assertFalse("Неправильный логин или пароль",wrongLoginOrPassword);
    }

}
