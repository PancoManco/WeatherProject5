package ru.pancoManco.weatherViewer.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.pancoManco.weatherViewer.model.Session;

import java.util.UUID;

@Repository
public class SessionRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Session session) {
        em.persist(session);
    }

    public Session findById(UUID id) {
        return em.find(Session.class, id);
    }

    public void delete(Session session) {
        em.remove(session);
    }
}
