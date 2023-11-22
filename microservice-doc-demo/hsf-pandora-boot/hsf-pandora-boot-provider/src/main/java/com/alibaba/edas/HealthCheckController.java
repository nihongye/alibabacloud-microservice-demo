package com.alibaba.edas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @Autowired
    private HealthService healthService;

    @RequestMapping("/health")
    public String healthCheck() throws Exception {
        return healthService.state();
    }

}
