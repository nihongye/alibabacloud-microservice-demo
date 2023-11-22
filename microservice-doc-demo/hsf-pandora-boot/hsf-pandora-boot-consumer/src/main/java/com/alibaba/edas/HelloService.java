package com.alibaba.edas;


public interface HelloService {
    String echo(String string)throws Exception;
    int sleep(int time)throws Exception;
    void runtimeException(String message)throws Exception;
    void exception(String message)throws Exception;
}
