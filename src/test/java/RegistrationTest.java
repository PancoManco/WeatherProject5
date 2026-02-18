import org.flywaydb.core.Flyway;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.pancoManco.weatherViewer.dto.UserRegisterDto;
import ru.pancoManco.weatherViewer.dto.UserSignInDto;
import ru.pancoManco.weatherViewer.model.User;
import ru.pancoManco.weatherViewer.service.LocationService;
import ru.pancoManco.weatherViewer.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.AssertionErrors.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfiguration.class)
public class RegistrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("Проверка регистрации пользователя и сохранения в БД")
    public void registerUser() {
        String login = "test"+Math.random();
        UserRegisterDto userRegisterDto =
                new UserRegisterDto(login,"password1","password1");
        userService.register(userRegisterDto);
        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM weather.users WHERE login = ?",
                Long.class,
                userRegisterDto.getUsername()
        );
        assertEquals(Long.valueOf(1), count);
        User savedUser = userService.getUserByUsername(login);
        assertNotNull("Новый зарегистрированный пользователь найден", savedUser);
    }

    @Test
    @DisplayName("Проверка при регистрации с неуникальным логином")
    public void nonUniqueUsernameRegister() {
        String username = "testUser";
        UserRegisterDto firstUser = new UserRegisterDto(username,"password1","password1");
        userService.register(firstUser);
        UserRegisterDto secondUser = new UserRegisterDto(username,"password2","password2");
        boolean existsBefore = userService.isAlreadyExist(secondUser);
        assertTrue("Пользователь с данным именем уже зарегистрирован", existsBefore);
    }

    @Test
    @DisplayName("Проверка на неправильность логина или пароля")
    public void checkingUsernameAndPassword() {
        String username = "test" + Math.random();
        UserRegisterDto userRegisterDto = new UserRegisterDto(username, "password1", "password1");
        userService.register(userRegisterDto);

        boolean correct = userService.isCorrectPasswordOrLogin(
                new UserSignInDto(username, "password1")
        );
        assertTrue("Правильный логин и пароль должны быть верными", correct);

        boolean wrongPassword = userService.isCorrectPasswordOrLogin(
                new UserSignInDto(username, "wrongPassword")
        );
        assertFalse("Неправильный пароль должен вернуть false", wrongPassword);

        boolean wrongLogin = userService.isCorrectPasswordOrLogin(
                new UserSignInDto("wrongUsername", "password1")
        );
        assertFalse("Неправильный логин должен вернуть false", wrongLogin);
    }

}
