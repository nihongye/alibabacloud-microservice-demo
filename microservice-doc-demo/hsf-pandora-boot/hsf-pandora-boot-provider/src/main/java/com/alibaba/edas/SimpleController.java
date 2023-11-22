package com.alibaba.edas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleController {
    @Autowired
    private HealthService healthService;

    @RequestMapping(value = "/health/update/{state}", method = RequestMethod.GET)
    public String updateHealth(@PathVariable String state) throws Exception{
        return healthService.update(state);
    }

    @RequestMapping(value = "/health/mock/threadPoolFull/{time}", method = RequestMethod.GET)
    public String mockThreadPoolFull(@PathVariable int time) throws Exception{
        return healthService.mockThreadPoolFull(time);
    }

    @RequestMapping(value = "/health/mock/OOM/{time}", method = RequestMethod.GET)
    public String mockOOM(@PathVariable int time) throws Exception{
        return healthService.mockOOM(time);
    }


}
