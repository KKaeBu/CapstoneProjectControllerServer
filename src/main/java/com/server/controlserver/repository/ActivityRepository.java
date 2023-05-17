package com.server.controlserver.repository;

import com.server.controlserver.domain.Activity;
import com.server.controlserver.domain.Pet;

import java.util.Optional;

public interface ActivityRepository {
    Activity save(Activity activity);
    Activity update(Activity activity);
    Optional<Activity> findById(Long id); //id로 찾아서 반환
}
