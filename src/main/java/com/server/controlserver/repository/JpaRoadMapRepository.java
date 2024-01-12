package com.server.controlserver.repository;

import com.server.controlserver.domain.Pet;
import com.server.controlserver.domain.RoadMap;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaRoadMapRepository implements RoadMapRepository {
  EntityManager em;

  public JpaRoadMapRepository(EntityManager em) {
    this.em = em;
  }

  @Override
  public RoadMap save(RoadMap roadMap) {
    em.persist(roadMap);
    return roadMap;
  }

  @Override
  public RoadMap update(RoadMap roadMap) {
    em.merge(roadMap);
    return roadMap;
  }

  @Override
  public Optional<RoadMap> findById(Long id) {
    RoadMap roadMap = em.find(RoadMap.class, id);
    return Optional.ofNullable(roadMap);
  }

  @Override
  public Optional<RoadMap> findByRoadMapName(String roadMapName) {
    List<RoadMap> result =
        em.createQuery("select r from RoadMap r where r.roadMapName = :roadMapName", RoadMap.class)
            .setParameter("roadMapName", roadMapName)
            .getResultList();
    return result.stream().findAny();
  }

  @Override
  public List<RoadMap> findAll() {
    List<RoadMap> result = em.createQuery("select r from RoadMap r", RoadMap.class).getResultList();
    return result;
  }
}
