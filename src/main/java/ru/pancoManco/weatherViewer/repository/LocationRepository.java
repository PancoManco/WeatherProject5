package ru.pancoManco.weatherViewer.repository;

import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Repository;
import ru.pancoManco.weatherViewer.model.Location;
import ru.pancoManco.weatherViewer.model.User;

import java.util.List;

@Repository
public class LocationRepository extends BaseRepository<Location> {

public int deleteLocationByIdAndUser(User user, Long id) {

    return em.createQuery("""
        DELETE FROM Location l
        WHERE l.id = :id
        AND l.userId = :user
    """)
            .setParameter("id", id)
            .setParameter("user", user)
            .executeUpdate();
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
