package com.server.controlserver.repository;

import com.server.controlserver.domain.Ping;
import com.server.controlserver.domain.RoadMap;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaPingRepository implements PingRepository{

    private final EntityManager em;
    private RoadMapRepository roadMapRepository;

    @Autowired
    public JpaPingRepository(EntityManager em, RoadMapRepository roadMapRepository) {
        this.em = em;
        this.roadMapRepository = roadMapRepository;
    }

    @Override
    public RoadMap save(List<Ping> pingList) {
        RoadMap roadMap = new RoadMap();
        roadMap.setRoadMapName("testRoadMapName");
        em.persist(roadMap);
//        roadMapRepository.save(roadMap);

        for(Ping ping : pingList){
            ping.setRoadMap(roadMap);
            // 로드맵의 핑리스트에 핑을 추가해주지 않으면
            // 로드맵에서 핑리스트에 핑이 참조가 되지 않음 (양방향 통신을 위해)
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
        List<Ping> result = em.createQuery("select p from Ping p", Ping.class)
                .getResultList();
        return result;
    }
}
