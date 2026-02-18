package ru.pancoManco.weatherViewer.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.pancoManco.weatherViewer.model.Location;
import ru.pancoManco.weatherViewer.model.User;

import java.util.List;

@Repository
@Slf4j
public class LocationRepository extends BaseRepository<Location> {

public int deleteLocationByIdAndUser(User user, Long id) {
     int deleted =em.createQuery("""
        DELETE FROM Location l
        WHERE l.id = :id
        AND l.userId = :user
    """)
            .setParameter("id", id)
            .setParameter("user", user)
            .executeUpdate();
     if (deleted == 0) {
         log.warn("Not found location with id to delete " + id);
     }
    log.debug("Successfully deleted location with idLocation {} and User: {}", id,user);
     return deleted;
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
