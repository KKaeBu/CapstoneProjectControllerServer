package com.server.controlserver.repository;

import com.server.controlserver.domain.GPS;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Optional<GPS> findLatest() {
        // 날짜상 가장 최근 데이터를 가져옴
        GPS gps = em.createQuery("select g from GPS g where g.createTime = (select MAX(g2.createTime) from GPS g2)", GPS.class)
                .getSingleResult();
        return Optional.ofNullable(gps);
    }
}
