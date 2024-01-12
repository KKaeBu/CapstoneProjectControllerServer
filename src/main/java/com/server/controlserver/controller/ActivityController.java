package com.server.controlserver.controller;

import com.server.controlserver.domain.Activity;
import com.server.controlserver.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
public class ActivityController {

  private final ActivityRepository activityRepository;

  @Autowired
  public ActivityController(ActivityRepository activityRepository) {
    this.activityRepository = activityRepository;
  }

  // 특정 산책의 활동량 가져오기
  @GetMapping("/api/activities/{activityId}")
  @ResponseBody
  public ResponseEntity<Optional<Activity>> getActivity(@PathVariable Long activityId) {
    return new ResponseEntity<Optional<Activity>>(
        activityRepository.findById(activityId), HttpStatus.OK);
  }
}
