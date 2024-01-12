package com.server.controlserver.repository;

import com.server.controlserver.domain.*;
import com.server.controlserver.dto.WalkResponseDto;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaWalkRepository implements WalkRepository {
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
  public void delete(Walk walk) {
    em.remove(walk);
  }

  @Override
  public void deleteByActivityId(Long activityId) {
    Walk walk =
        em.createQuery("select  w from Walk w where w.activity.id = :activityId", Walk.class)
            .setParameter("activityId", activityId)
            .getSingleResult();
    em.remove(walk);
  }

  @Override
  public Optional<Walk> findById(Long id) {
    Walk walk = em.find(Walk.class, id);
    return Optional.ofNullable(walk);
  }

  @Override
  public List<Walk> findAllByPetId(Long petId) {
    List<Walk> walkList =
        em.createQuery("select w from Walk w where w.pet.id = :petId", Walk.class)
            .setParameter("petId", petId)
            .getResultList();

    return walkList;
  }

  @Override
  public Optional<Walk> findByRoadMapId(Long id) {
    Walk result =
        em.createQuery("select w from Walk w where w.roadMap.id = :id", Walk.class)
            .setParameter("road_map", id)
            .getSingleResult();
    return Optional.ofNullable(result);
  }

  @Override
  public List<Walk> findByPetId(Long petId) {
    List<Walk> result =
        em.createQuery("select w from Walk w where w.pet.id = :petId", Walk.class)
            .setParameter("petId", petId)
            .getResultList();
    return result;
  }

  @Override
  public List<Walk> findAll() {
    List<Walk> result = em.createQuery("select w from Walk w", Walk.class).getResultList();

    return result;
  }

  @Override
  public Optional<Walk> lastestWalkFindByPetId(Long petId) {
    Walk lastestWalk =
        em.createQuery(
                "select w from Walk w where w.pet.id = :petId order by w.walkDate desc", Walk.class)
            .setParameter("petId", petId)
            .setMaxResults(1) // 한개만 가져오도록 지정
            .getSingleResult();

    return Optional.ofNullable(lastestWalk);
  }

  @Override
  public List<Ping> coordsListFindByWalkId(Long walkId) {
    RoadMap rm =
        (RoadMap)
            em.createQuery("select w.roadMap from Walk w where w.id = :walkId")
                .setParameter("walkId", walkId)
                .getSingleResult();

    return rm.getPingList();
  }
}
