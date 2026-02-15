package ru.pancoManco.weatherViewer.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public abstract class BaseRepository <T> {
    @PersistenceContext
    protected EntityManager em;

    public void save (T entity) {
        em.persist(entity);
    }
}
