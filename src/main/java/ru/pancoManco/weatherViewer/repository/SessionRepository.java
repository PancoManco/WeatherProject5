package ru.pancoManco.weatherViewer.repository;

import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Repository;
import ru.pancoManco.weatherViewer.model.Session;
import ru.pancoManco.weatherViewer.model.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public class SessionRepository extends BaseRepository<Session> {

    public Optional<Session> findById(UUID id) {
        try {
            Session result  = em.createQuery(
                            "SELECT s FROM Session s WHERE s.id = :uuid",
                            Session.class
                    ).setParameter("uuid", id)
                    .getSingleResult();
            return Optional.of(result);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public void deleteByUser(User user) {
            int deleted = em.createQuery("DELETE FROM Session s WHERE s.userId = :userId")
                    .setParameter("userId", user.getId())
                    .executeUpdate();
        if (deleted == 0) {
            throw new NoResultException("No session found with User " + user.getLogin());
        }
    }

    public void deleteById(UUID id) {
            int deleted = em.createQuery("DELETE FROM Session s WHERE s.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
        if (deleted == 0) {
            throw new NoResultException("No session found with id " + id);
        }
    }

    public Optional<User> findUserById(UUID sessionId) {
        try {
            User result  = em.createQuery(
                            "SELECT s FROM Session s WHERE s.id = :uuid",
                            User.class
                    ).setParameter("uuid", sessionId)
                    .getSingleResult();
            return Optional.of(result);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
