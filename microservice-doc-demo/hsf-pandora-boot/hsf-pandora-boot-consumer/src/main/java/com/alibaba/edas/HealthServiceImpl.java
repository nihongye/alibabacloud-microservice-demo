package com.alibaba.edas;

import com.alibaba.boot.hsf.annotation.HSFProvider;

@HSFProvider(serviceInterface = HealthService.class, serviceVersion = "1.0.0")
public class HealthServiceImpl implements HealthService {
    private String state = "UP";

    @Override public String state() throws Exception {
        if (state.contains("exception")) {
            throw new RuntimeException(state);
        }
        return state;
    }

    @Override public String update(String state) {
        String oldState = this.state;
        this.state = state;
        return oldState;
    }
}
