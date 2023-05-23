package com.server.controlserver.repository;

import com.server.controlserver.domain.*;
import com.server.controlserver.dto.WalkResponseDto;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaWalkRepository implements WalkRepository{
    EntityManager em;

    public JpaWalkRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Walk save(Walk walk, Pet pet) {
        // Walk 저장
        walk.setPet(pet);
        walk.getPet().getWalkList().add(walk);
        em.persist(walk);
        return walk;
    }

    @Override
    public Walk update(Walk walk) {
        em.merge(walk);
        return walk;
    }

    @Override
    public Walk delete(Walk walk){
        em.remove(walk);
        return walk;
    }

    @Override
    public Optional<Walk> findById(Long id) {
        Walk walk = em.find(Walk.class, id);
        return Optional.ofNullable(walk);
    }

    @Override
    public Optional<Walk> findByRoadMapId(Long id) {
        Walk result = em.createQuery("select w from Walk w where w.roadMap.id = :id", Walk.class)
                .setParameter("id",id)
                .getSingleResult();
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<Walk> lastestWalkFindByPetId(Long petId) {
        Walk lastestWalk = em.createQuery("select w from Walk w where (w.pet.id = :petId and w.walkDate = (select MAX(w2.walkDate) from Walk w2))", Walk.class)
                .setParameter("petId",petId)
                .getSingleResult();


        return Optional.ofNullable(lastestWalk);

    }

    @Override
    public List<Walk> findAll() {
        List<Walk> result = em.createQuery("select w from Walk w", Walk.class)
                .getResultList();
        return result;
    }
}
