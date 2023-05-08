package com.server.controlserver.controller;

import com.server.controlserver.service.WalkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class WalkController {

    private WalkService walkService;

    @Autowired
    public WalkController(WalkService walkService) {
        this.walkService = walkService;
    }


}
