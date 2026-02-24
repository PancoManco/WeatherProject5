import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
import ru.pancoManco.weatherViewer.model.Session;
import ru.pancoManco.weatherViewer.model.User;
import ru.pancoManco.weatherViewer.repository.SessionRepository;
import ru.pancoManco.weatherViewer.service.SessionService;
import ru.pancoManco.weatherViewer.service.UserService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

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

    @Autowired
    private SessionRepository sessionRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    @DisplayName("Authentication should create exactly one session for the user")
    public void authenticate_shouldCreateSingleSessionForUser() {
        String login = "test1";
        UserRegisterDto userRegisterDto =
                new UserRegisterDto(login,"password1","password1");
        userService.register(userRegisterDto);
        UserSignInDto userSignInDto = new UserSignInDto(userRegisterDto.getUsername(),userRegisterDto.getPassword());
        userService.authenticate(userSignInDto);
        User user = userService.getUserByUsername(userSignInDto.getUsername());
        Long userId = user.getId();
        em.flush();
        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM weather.sessions WHERE user_id = ?",
                Long.class,
                userId
        );
        assertEquals(1L, count,  "Exactly one session should be created for the authenticated user");
    }
    @Test
    @DisplayName("Session is invalid if expired")
    public void isSessionValid_withExpiredSession_shouldReturnFalse() {
        User user = new User();
        user.setLogin("test2");
        user.setPassword("password2");
        em.persist(user);
        Session expiredSession = new Session(UUID.randomUUID(), user, LocalDateTime.now().minusMinutes(10));
        em.persist(expiredSession);
        em.flush();
        boolean valid = sessionService.isSessionValid(expiredSession.getId());
        assertFalse(valid, "Expired session should not be valid");
    }

    @Test
    @DisplayName("Expired sessions are deleted by cleanup")
    public void cleanupExpiredSessions_shouldRemoveExpiredAndKeepValid() {

        User user = new User();
        user.setLogin("user3");
        user.setPassword("password3");
        em.persist(user);
        Session expired = new Session(UUID.randomUUID(), user, LocalDateTime.now().minusHours(1));
        Session valid = new Session(UUID.randomUUID(), user, LocalDateTime.now().plusHours(1));
        em.persist(expired);
        em.persist(valid);
        em.flush();
        sessionService.cleanupExpiredSessions();
        em.flush();
        Optional<Session> expiredCheck = sessionRepository.findById(expired.getId());
        Optional<Session> validCheck = sessionRepository.findById(valid.getId());

        assertFalse(expiredCheck.isPresent(), "Expired session should be deleted");
        assertTrue(validCheck.isPresent(), "Valid session should remain");
    }

    @Test
    @DisplayName("Expired sessions are not considered valid")
    public void findValidSession_withExpiredSession_shouldReturnEmpty() {
        User user = new User();
        user.setLogin("user4");
        user.setPassword("password4");
        em.persist(user);
        Session session = new Session(UUID.randomUUID(), user, LocalDateTime.now().minusMinutes(5));
        em.persist(session);
        em.flush();
        Optional<Session> validSession = sessionRepository.findValidSession(session.getId());
        assertFalse(validSession.isPresent(), "Expired session should not be considered valid");
    }
}
