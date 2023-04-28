package com.server.controlserver.repository;

import com.server.controlserver.domain.Activity;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class JpaActivityRepository implements ActivityRepository{
    private EntityManager em;

    public JpaActivityRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Activity save(Activity activity) {
        em.persist(activity);
        return activity;
    }
}
