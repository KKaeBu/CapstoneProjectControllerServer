package com.server.controlserver.service;

import com.server.controlserver.repository.WalkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalkService {

    private WalkRepository walkRepository;

    @Autowired
    public WalkService(WalkRepository walkRepository) {
        this.walkRepository = walkRepository;
    }
}
