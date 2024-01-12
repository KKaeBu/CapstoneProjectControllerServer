package com.server.controlserver.repository;

import com.server.controlserver.domain.Activity;
import com.server.controlserver.domain.Pet;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaActivityRepository implements ActivityRepository {
  private EntityManager em;

  public JpaActivityRepository(EntityManager em) {
    this.em = em;
  }

  @Override
  public Activity save(Activity activity) {
    em.persist(activity);
    return activity;
  }

  @Override
  public Activity update(Activity activity) {
    em.merge(activity);
    return activity;
  }

  @Override
  public Optional<Activity> findById(Long id) {
    Activity activity = em.find(Activity.class, id);
    return Optional.ofNullable(activity);
  }
}
