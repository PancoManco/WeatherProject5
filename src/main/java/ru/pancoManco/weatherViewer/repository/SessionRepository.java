package ru.pancoManco.weatherViewer.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.pancoManco.weatherViewer.model.Session;
import ru.pancoManco.weatherViewer.model.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public class SessionRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Session session) {
        em.persist(session);
    }

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

    public void deleteByUserId(User user) {
        try {
            em.createQuery("DELETE FROM Session s WHERE s.userId = :userId")
                    .setParameter("userId", user.getId())
                    .executeUpdate();
        }
        catch (NoResultException e) {
            throw new NoResultException("No session found with User " + user.getLogin());
        }
    }

    public void deleteById(UUID id) {
        try {
            em.createQuery("DELETE FROM Session s WHERE s.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
        }
        catch (NoResultException e) {
            throw new NoResultException("No session found with id " + id);
        }
    }
}
