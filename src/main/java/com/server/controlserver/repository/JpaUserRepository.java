package com.server.controlserver.repository;

import com.server.controlserver.domain.User;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaUserRepository implements UserRepository {
  EntityManager em;

  public JpaUserRepository(EntityManager em) {
    this.em = em;
  }

  @Override
  public User save(User user) {
    em.persist(user);
    return user;
  }

  @Override
  public User update(User user) {
    em.merge(user);
    return user;
  }

  @Override
  public User delete(User user) {
    em.remove(user);
    return user;
  }

  @Override
  public Optional<User> findById(Long id) {
    User user = em.find(User.class, id);
    return Optional.ofNullable(user);
  }

  @Override
  public Optional<User> findByName(String name) {
    List<User> result =
        em.createQuery("select u from User u where u.name = :name", User.class)
            .setParameter("name", name)
            .getResultList();
    return result.stream().findAny();
  }

  @Override
  public Optional<User> findByUserId(String userId) {
    User user =
        em.createQuery("select u from User u where u.userId = :userId", User.class)
            .setParameter("userId", userId)
            .getSingleResult();

    return Optional.ofNullable(user);
  }

  @Override
  public List<User> findAll() {
    List<User> result = em.createQuery("select u from User u", User.class).getResultList();
    return result;
  }
}
