package com.alibaba.edas;


public interface HealthService {
    String state()throws Exception;

    String update(String state);
}
