package com.alibaba.edas;

import com.alibaba.boot.hsf.annotation.HSFProvider;
import com.taobao.hsf.common.Env;
import com.taobao.hsf.context.RPCContext;
import com.taobao.hsf.util.HSFServiceContainer;

@HSFProvider(serviceInterface = HelloService.class, serviceVersion = "1.0.0") public class HelloServiceImpl
    implements HelloService {

    @Override public String echo(String string) throws Exception {
        Env env = HSFServiceContainer.getInstance(Env.class);
        return "return " +  string + " by server " + env.getBindHost() + ":" + env.getBindPort();
    }

    @Override public int sleep(int time) throws Exception {
        Thread.sleep(5000);
        return time;
    }

    @Override public void runtimeException(String message) throws Exception {
        throw new RuntimeException(message);
    }

    @Override public void exception(String message) throws Exception {
        throw new Exception(message);
    }
}
