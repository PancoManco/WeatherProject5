package ru.pancoManco.weatherViewer.repository;


import jakarta.persistence.NoResultException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.pancoManco.weatherViewer.model.User;

import java.util.Optional;

@Slf4j
@Repository
public class UserRepository extends BaseRepository<User> {

    public Optional<User> findByUsername(String login) {
        try {
            User user = em.createQuery(
                            "SELECT u FROM User u WHERE u.login = :login",
                            User.class
                    ).setParameter("login", login)
                    .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            log.debug("No user found for login {}", login);
            return Optional.empty();
        }
    }
}
