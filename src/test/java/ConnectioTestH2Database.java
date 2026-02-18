
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ContextConfiguration(classes = TestConfiguration.class)
@ExtendWith(SpringExtension.class)
@Transactional
public class ConnectioTestH2Database {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testConnection() {
        jdbcTemplate.execute("INSERT into weather.users (login, password) values ('Fedya', '123456789')");
        jdbcTemplate.execute("INSERT into weather.users (login, password) values ('Fedya1', '12345678910')");
       Long counter =  jdbcTemplate.queryForObject("SELECT COUNT(*) from weather.users",Long.class);
       assertEquals(Long.valueOf(2),counter);
    }
}
