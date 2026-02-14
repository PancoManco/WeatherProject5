package ru.pancoManco.weatherViewer.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import ru.pancoManco.weatherViewer.model.Location;
import ru.pancoManco.weatherViewer.model.User;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class LocationRepository {

    @PersistenceContext
    private EntityManager em;

    public void saveLocationForUser(Location location) {
        em.persist(location);
    }

public void deleteLocationForUser(User user,Long id) {

    int deleted = em.createQuery("""
        DELETE FROM Location l
        WHERE l.id = :id
        AND l.userId = :user
    """)
            .setParameter("id", id)
            .setParameter("user", user)
            .executeUpdate();

    if (deleted == 0) {
        throw new NoResultException("Location not found");
    }
}

    public List<Location> getAllUserLocations(User user) {
        return em.createQuery(
                        "SELECT l FROM Location l WHERE l.userId = :user",
                        Location.class
                )
                .setParameter("user", user)
                .getResultList();
    }
}
