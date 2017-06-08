package com.parrot.proxies;

import com.parrot.InvocationInfo;
import com.parrot.RestfulService;
import com.parrot.ResponseStorage;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RestfulServiceResponseRecorderTests {
    private static final String PATH = "/places/parks";
    private static final String RESPONSE = "Test response";

    @Test
    public void works(){
        ResponseStorage responseStorage = mock(ResponseStorage.class);
        RestfulService serviceProxy = getServiceProxy(RESPONSE, responseStorage);
        SortedMap<String, String> queryParameters = getQueryParameters();
        String actual = serviceProxy.get(PATH, queryParameters);
        Assert.assertEquals(RESPONSE, actual);
        InvocationInfo invocationInfo = new InvocationInfo("get", PATH, queryParameters);
        verify(responseStorage, times(1)).save(eq(invocationInfo), eq(RESPONSE));
    }

    private SortedMap<String, String> getQueryParameters() {
        SortedMap<String, String> parameters = new TreeMap<>();
        parameters.put("zipcode", "60540");
        return parameters;
    }

    private RestfulService getServiceProxy(String testResponse, ResponseStorage responseStorage) {
        RestfulService service = mock(RestfulService.class);
        when(service.get(any(), any())).thenReturn(testResponse);
        RestfulServiceResponseRecorder recorder = new RestfulServiceResponseRecorder(service, responseStorage);
        return recorder.getProxy();
    }
}
