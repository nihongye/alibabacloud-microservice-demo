package com.alibaba.edas;

import com.alibaba.boot.hsf.annotation.HSFProvider;

@HSFProvider(serviceInterface = ClientService.class, serviceVersion = "1.0.0")
public class ClientServiceImpl implements ClientService {

    @Override public String echo(String string) throws Exception {
        return string;
    }
}
