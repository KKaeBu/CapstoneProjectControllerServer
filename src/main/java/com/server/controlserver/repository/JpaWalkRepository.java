package com.server.controlserver.repository;

import com.server.controlserver.domain.Pet;
import com.server.controlserver.domain.Walk;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaWalkRepository implements WalkRepository{
    EntityManager em;

    public JpaWalkRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Walk save(Walk walk) {
        em.persist(walk);
        return walk;
    }

    @Override
    public Walk update(Walk walk) {
        em.merge(walk);
        return walk;
    }

    @Override
    public Optional<Walk> findById(Long id) {
        Walk walk = em.find(Walk.class, id);
        return Optional.ofNullable(walk);
    }

    @Override
    public Optional<Walk> findByRoadMapId(Long id) {
        Walk result = em.createQuery("select w from Walk w where w.roadMapId = :id", Walk.class)
                .setParameter("road_map_id",id)
                .getSingleResult();
        return Optional.ofNullable(result);
    }

    @Override
    public List<Walk> findAll() {
        List<Walk> result = em.createQuery("select w from Walk w", Walk.class)
                .getResultList();
        return result;
    }
}
