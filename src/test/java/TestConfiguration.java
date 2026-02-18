import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.flywaydb.core.Flyway;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;
import ru.pancoManco.weatherViewer.repository.LocationRepository;
import ru.pancoManco.weatherViewer.repository.SessionRepository;
import ru.pancoManco.weatherViewer.repository.UserRepository;
import ru.pancoManco.weatherViewer.service.LocationService;
import ru.pancoManco.weatherViewer.service.OpenWeatherService;
import ru.pancoManco.weatherViewer.service.UserService;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = {"ru.pancoManco.weatherViewer.service","ru.pancoManco.weatherViewer.mapper"})
@EnableJpaRepositories("ru.pancoManco.weatherViewer.repository")
@EnableTransactionManagement
public class TestConfiguration {

    @PersistenceContext
    private EntityManager em;

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig("test.properties");
        return new HikariDataSource(config);
    }
    @Bean
    public Flyway flyway(DataSource dataSource) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .load();
        flyway.migrate();
        return flyway;
    }

    @Bean
    @DependsOn("flyway")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("ru.pancoManco.weatherViewer.model");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaProperties(hibernateProperties());
        em.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        return em;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.format_sql", "true");
        properties.put("hibernate.hbm2ddl.auto", "create-drop");
        return properties;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public Validator validator() {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory();
        return validatorFactory.getValidator();

    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    public LocationRepository locationRepository() {
        return new LocationRepository() {{
            this.em = em;
        }};
    }

    @Bean
    public LocationService locationService(LocationRepository locationRepository,
                                           OpenWeatherService openWeatherService,
                                           UserService userService,
                                           jakarta.validation.Validator validator) {
        return new LocationService(locationRepository, openWeatherService, userService, validator);
    }

    @Bean
    public SessionRepository sessionRepository() {
        return new SessionRepository() {
            {
                this.em = em;
            }
        };
    }

    @Bean
    public UserRepository userRepository() {
        return new UserRepository() {
            {
                this.em = em;
            }
        };
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }


    @Bean
    public OpenWeatherService openWeatherService(RestTemplate restTemplate,
                                                 Validator validator,
                                                 ObjectMapper objectMapper) {
        return new OpenWeatherService(restTemplate, objectMapper, validator);
    }
}
