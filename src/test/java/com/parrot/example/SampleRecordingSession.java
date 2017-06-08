package com.parrot.example;

import com.parrot.ResponseStorage;
import com.parrot.RestfulService;
import com.parrot.proxies.RecordedResponsePlayback;
import com.parrot.proxies.RestfulServiceResponseRecorder;
import com.parrot.storage.ResponseStorageImpl;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SampleRecordingSession {
    final String basePath = "src/test/resources/recorded";

    @Ignore
    @Test
    public void recordsCallsToRestfulServiceToActualFiles(){
        RestfulService restfulService = new SampleRestfulService();
        ResponseStorage responseStorage = new ResponseStorageImpl(basePath);
        RestfulServiceResponseRecorder responseRecorder = new RestfulServiceResponseRecorder(restfulService, responseStorage);
        RestfulService serviceProxy = responseRecorder.getProxy();
        List<Map<String, String>> stringsToConcatenate = getStringsToConcatenate();
        stringsToConcatenate.forEach(parameters -> serviceProxy.get(SampleRestfulService.STRINGS_CONCATENATE, parameters));
        List<Map<String, String>> numbersToDivide = getNumbersToDivide();
        numbersToDivide.forEach(parameters -> serviceProxy.get(SampleRestfulService.MATH_DIVIDE, parameters));
    }

    @Ignore
    @Test
    public void readsRecordedCalls(){
        ResponseStorage responseStorage = new ResponseStorageImpl(basePath);
        RecordedResponsePlayback recordedResponsePlayback = new RecordedResponsePlayback(responseStorage);
        RestfulService serviceProxy = recordedResponsePlayback.getProxy();
        List<Map<String, String>> stringsToConcatenate = getStringsToConcatenate();
        stringsToConcatenate.forEach(parameters ->
                System.out.println(serviceProxy.get(SampleRestfulService.STRINGS_CONCATENATE, parameters)));
        List<Map<String, String>> numbersToDivide = getNumbersToDivide();
        numbersToDivide.forEach(parameters ->
                System.out.println(serviceProxy.get(SampleRestfulService.MATH_DIVIDE, parameters)));
    }

    private List<Map<String, String>> getStringsToConcatenate(){
        return Arrays.asList(
                getMap("token1", "color", "token2", "size"),
                getMap("token1", "length", "token2", "width")
        );
    }

    private List<Map<String, String>> getNumbersToDivide(){
        return Arrays.asList(
                getMap("numerator", "1.5", "denominator", "0.3"),
                getMap("numerator", "1.2", "denominator", "0")
        );
    }

    Map<String, String> getMap(String key1, String value1, String key2, String value2){
        Map<String, String> ret = new HashMap<>();
        ret.put(key1, value1);
        ret.put(key2, value2);
        return ret;
    }
}
