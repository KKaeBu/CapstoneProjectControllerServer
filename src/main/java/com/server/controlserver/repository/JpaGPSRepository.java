package com.server.controlserver.repository;

import com.server.controlserver.domain.GPS;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaGPSRepository implements GPSRepository{

    private final EntityManager em;

    public JpaGPSRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public GPS save(GPS gps) {
        em.persist(gps);
        return gps;
    }

    @Override
    public GPS update(GPS gps) {
        em.merge(gps);
        return gps;
    }

    @Override
    public Optional<GPS> findLastest() {
        return Optional.empty();
    }
}
