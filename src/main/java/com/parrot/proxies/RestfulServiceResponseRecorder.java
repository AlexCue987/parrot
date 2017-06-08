package com.parrot.proxies;

import com.parrot.InvocationInfo;
import com.parrot.RestfulService;
import com.parrot.ResponseStorage;

import java.lang.reflect.Method;

public class RestfulServiceResponseRecorder extends RestfulServiceWrapper {
    private final ResponseStorage responseStorage;

    public RestfulServiceResponseRecorder(RestfulService service, ResponseStorage responseStorage) {
        super(service);
        this.responseStorage = responseStorage;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = method.invoke(service, args);
        InvocationInfo invocationInfo = getInvocationInfo(method.getName(), args);
        responseStorage.save(invocationInfo, result.toString());
        return result;
    }
}
