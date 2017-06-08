package com.parrot.proxies;

import com.parrot.InvocationInfo;
import com.parrot.RestfulService;
import com.parrot.ResponseStorage;

import java.lang.reflect.Method;

public class RecordedResponsePlayback extends RestfulServiceWrapper {
    private final ResponseStorage responseStorage;

    public RecordedResponsePlayback(ResponseStorage responseStorage) {
        super(noRestfulServiceNeeded());
        this.responseStorage = responseStorage;
    }

    private static RestfulService noRestfulServiceNeeded(){return null;}

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        InvocationInfo invocationInfo = getInvocationInfo(method.getName(), args);
        String result = responseStorage.read(invocationInfo);
        return result;
    }
}
