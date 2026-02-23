package ru.pancoManco.weatherViewer.repository;

import jakarta.persistence.NoResultException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.pancoManco.weatherViewer.model.Session;
import ru.pancoManco.weatherViewer.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Slf4j
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
            log.warn("No session found with UUID {}", id);
          return  Optional.empty();
        }
    }

    public int deleteByUser(User user) {
        int deleted = em.createQuery("DELETE FROM Session s WHERE s.userId = :userId")
                .setParameter("userId", user.getId())
                .executeUpdate();
        if (deleted == 0) {
            log.warn("No session found with user {} to deleted", user);
        }
        log.debug("Successfully deleted {} sessions for user {}", deleted, user);
        return deleted;
    }


    public void deleteById(UUID id) {
        int deleted = em.createQuery("DELETE FROM Session s WHERE s.id = :id")
                .setParameter("id", id)
                .executeUpdate();
        if (deleted == 0) {
            log.warn("No session found with UUID to delete: {}", id);
        }
        log.debug("Successfully deleted session with UUID: {}", id);
    }


    public int deleteByExpiresAtBefore(LocalDateTime now) {
        int deleted = em.createQuery("DELETE FROM Session s WHERE s.expiresAt < :now")
                .setParameter("now", now)
                .executeUpdate();
        if (deleted == 0) {
            log.warn("Not found expired sessions to clean-up {}", deleted);
        }
        log.debug("Successfully deleted {} expired sessions", deleted);
        return deleted;
    }

    public Optional<Session> findValidSession(UUID id) {
        List<Session> result = em.createQuery("""
            SELECT s
            FROM Session s
            WHERE s.id = :id
            AND s.expiresAt > :now
            """, Session.class)
                .setParameter("id", id)
                .setParameter("now", LocalDateTime.now())
                .getResultList();

        return result.stream().findFirst();
    }
}
