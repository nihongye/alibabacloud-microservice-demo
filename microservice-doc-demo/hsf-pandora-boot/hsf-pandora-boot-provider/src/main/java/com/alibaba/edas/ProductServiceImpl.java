package com.alibaba.edas;

import com.alibaba.boot.hsf.annotation.HSFProvider;

@HSFProvider(serviceInterface = ProductService.class, serviceVersion = "1.0.0")
public class ProductServiceImpl implements ProductService {
    @Override public String getInfo(String name) throws Exception {
        return name + " is exists";
    }
}
