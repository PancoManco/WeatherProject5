
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.pancoManco.weatherViewer.dto.UserRegisterDto;
import ru.pancoManco.weatherViewer.dto.UserSignInDto;
import ru.pancoManco.weatherViewer.model.User;
import ru.pancoManco.weatherViewer.service.UserService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.AssertionErrors.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfiguration.class)
@Transactional
public class RegistrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("User registration should create a record in the database")
    public void register_shouldCreateUserInDatabase() {
        String login = "test" + UUID.randomUUID();
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
        assertNotNull("Newly registered user should be found", savedUser);
    }

    @Test
    @DisplayName("Registration with duplicate username should detect existing user")
    public void register_withDuplicateUsername_shouldDetectExistingUser() {
        String username = "testUser";
        UserRegisterDto firstUser = new UserRegisterDto(username,"password1","password1");
        userService.register(firstUser);
        UserRegisterDto secondUser = new UserRegisterDto(username,"password2","password2");
        boolean existsBefore = userService.isAlreadyExist(secondUser);
        assertTrue("User with this username should already exist", existsBefore);
    }

    @Test
    @DisplayName("Authentication should validate username and password correctly")
    public void isCorrectPasswordOrLogin_shouldValidateCredentials() {
        String username = "test" + UUID.randomUUID();
        UserRegisterDto userRegisterDto = new UserRegisterDto(username, "password1", "password1");
        userService.register(userRegisterDto);

        boolean correct = userService.isCorrectPasswordOrLogin(
                new UserSignInDto(username, "password1")
        );
        assertTrue("Valid username and password should return true", correct);

        boolean wrongPassword = userService.isCorrectPasswordOrLogin(
                new UserSignInDto(username, "wrongPassword")
        );
        assertFalse("Invalid password should return false", wrongPassword);

        boolean wrongLogin = userService.isCorrectPasswordOrLogin(
                new UserSignInDto("wrongUsername", "password1")
        );
        assertFalse("Invalid username should return false", wrongLogin);
    }

}
