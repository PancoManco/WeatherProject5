package ru.pancoManco.weatherViewer.repository;


import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.pancoManco.weatherViewer.model.User;

import java.util.Optional;

@Repository
public class UserRepository {

    @PersistenceContext
    EntityManager em;

    public void save(User user) {
        em.persist(user);
    }

    public Optional<User> findByUsername(String login) {
        try {
            User user = em.createQuery(
                            "SELECT u FROM User u WHERE u.login = :login",
                            User.class
                    ).setParameter("login", login)
                    .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
