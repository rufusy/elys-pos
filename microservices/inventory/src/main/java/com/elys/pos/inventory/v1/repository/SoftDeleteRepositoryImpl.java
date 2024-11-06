package com.elys.pos.inventory.v1.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class SoftDeleteRepositoryImpl<T> implements SoftDeleteRepository<T> {

    @PersistenceContext
    private EntityManager entityManager;

    private final Class<T> entityType;

    public SoftDeleteRepositoryImpl(Class<T> entityType) {
//        this.entityType = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.entityType = entityType;
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAllActive() {
        String queryString = "SELECT e FROM " + entityType.getSimpleName() + " e WHERE  e.deleted = false";
        TypedQuery<T> query = entityManager.createQuery(queryString, entityType);
        return query.getResultList();
    }
}
