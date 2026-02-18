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
import ru.pancoManco.weatherViewer.scheduler.SessionCleanupScheduler;
import ru.pancoManco.weatherViewer.service.SessionService;
import ru.pancoManco.weatherViewer.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfiguration.class)
@Transactional
public class SessionCreatingAndExpiringTest {

    @Autowired
    private UserService userService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SessionService sessionService;


  //  private SessionCleanupScheduler sessionCleanupScheduler;

    @Test
    @DisplayName("Проверка что создается новая сессия при авторизации пользователя")
    public void createNewSession() {

        String login = "test"+Math.random();
        UserRegisterDto userRegisterDto =
                new UserRegisterDto(login,"password1","password1");
        userService.register(userRegisterDto);

        UserSignInDto userSignInDto = new UserSignInDto(userRegisterDto.getUsername(),userRegisterDto.getPassword());

        userService.authenticate(userSignInDto);
        User user = userService.getUserByUsername(userSignInDto.getUsername());
        Long userId = user.getId();
        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM weather.sessions WHERE user_id = ?",
                Long.class,
                userId
        );
        assertEquals(1L, count, "Должна быть создана ровно одна сессия для пользователя");
    }

}
