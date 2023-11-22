package com.alibaba.edas;


public interface HealthService {
    String state();

    String update(String state);

    String mockThreadPoolFull(int worktime);

    String mockOOM(int worktime);
}
