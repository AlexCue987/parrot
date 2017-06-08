package com.parrot.proxies;

import com.parrot.InvocationInfo;
import com.parrot.RestfulService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public abstract class RestfulServiceWrapper implements InvocationHandler {
    public static final String METHOD = "method";
    protected final RestfulService service;

    public RestfulServiceWrapper(RestfulService service) {
        this.service = service;
    }

    public RestfulService getProxy(){
        RestfulService ret = (RestfulService) Proxy.newProxyInstance(RestfulService.class.getClassLoader(),
                new Class[]{RestfulService.class}, this);
        return ret;
    }

    @Override
    public abstract Object invoke(Object proxy, Method method, Object[] args) throws Throwable;

    public static InvocationInfo getInvocationInfo(String methodName, Object[] args) {
        String queryPath = args[0].toString();
        Object queryParameters = args[1];
        SortedMap<String, String> sortedQueryParameters = new TreeMap<>((Map<String, String>)queryParameters);
        return new InvocationInfo(methodName, queryPath, sortedQueryParameters);
    }
}
