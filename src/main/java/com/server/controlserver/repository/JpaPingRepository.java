package com.server.controlserver.repository;

import com.server.controlserver.domain.Pet;
import com.server.controlserver.domain.Ping;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaPingRepository implements PingRepository{

    private final EntityManager em;

    public JpaPingRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Ping save(Ping ping) {
        em.persist(ping);
        return ping;
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
