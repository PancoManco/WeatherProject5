package ru.pancoManco.weatherViewer.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseRepository <T> {
    @PersistenceContext
    protected EntityManager em;

    public void save (T entity) {
        log.debug("save {}", entity);
        em.persist(entity);
        log.debug("Successfully saved {}", entity);
    }
}
