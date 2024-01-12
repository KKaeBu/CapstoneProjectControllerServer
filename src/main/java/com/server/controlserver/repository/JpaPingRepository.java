package com.server.controlserver.repository;

import com.server.controlserver.domain.Ping;
import com.server.controlserver.domain.RoadMap;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaPingRepository implements PingRepository {

  private final EntityManager em;

  @Autowired
  public JpaPingRepository(EntityManager em) {
    this.em = em;
  }

  @Override
  public Ping savePing(Ping ping) {
    em.persist(ping);
    return ping;
  }

  @Override
  public RoadMap saveRoadMapPingList(RoadMap roadMap, List<Ping> pingList) {
    // RoadMap 저장
    em.persist(roadMap);

    System.out.println("pingList: " + pingList);
    // Ping 저장
    for (Ping ping : pingList) {
      // 각 핑이 어떤 RoadMap 핑인지를 명시
      ping.setRoadMap(roadMap);
      // RoadMap에도 어떤 핑이 들어가는지를 명시
      ping.getRoadMap().getPingList().add(ping);
      em.persist(ping);
    }

    return roadMap;
  }

  @Override
  public Ping update(Ping ping) {
    em.merge(ping);
    return ping;
  }

  @Override
  public Optional<Ping> findById(Long id) {
    Ping ping = em.find(Ping.class, id);
    return Optional.ofNullable(ping);
  }

  @Override
  public List<Ping> findAll() {
    List<Ping> result = em.createQuery("select p from Ping p", Ping.class).getResultList();
    return result;
  }
}
