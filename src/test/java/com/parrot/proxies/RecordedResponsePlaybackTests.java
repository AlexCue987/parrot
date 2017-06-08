package com.parrot.proxies;

import com.parrot.InvocationInfo;
import com.parrot.RestfulService;
import com.parrot.ResponseStorage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RecordedResponsePlaybackTests {
    private static final String WEATHER_PATH = "/current/weather/";
    private static final String TRAFFIC_PATH = "/current/traffic/";

    private static final String WEATHER = "Overcast";
    private static final String TRAFFIC = "Light";

    private final Map<String, String> queryParameters = new HashMap<>();

    private ResponseStorage responseStorage;

    @Before
    public void setup(){
        responseStorage = mock(ResponseStorage.class);
        InvocationInfo weatherInvocationInfo = RestfulServiceWrapper.getInvocationInfo("get", getArgs(WEATHER_PATH));
        when(responseStorage.read(weatherInvocationInfo)).thenReturn(WEATHER);
        InvocationInfo trafficInvocationInfo = RestfulServiceWrapper.getInvocationInfo("get", getArgs(TRAFFIC_PATH));
        when(responseStorage.read(trafficInvocationInfo)).thenReturn(TRAFFIC);
    }

    @Test
    public void readsCorrectWeatherResponse(){
        RestfulService serviceProxy = getServiceProxy();
        String actualWeatherResponse = serviceProxy.get(WEATHER_PATH, queryParameters);
        Assert.assertEquals(WEATHER, actualWeatherResponse);
    }

    @Test
    public void readsCorrectTrafficResponse(){
        RestfulService serviceProxy = getServiceProxy();
        String actualTrafficResponse = serviceProxy.get(TRAFFIC_PATH, queryParameters);
        Assert.assertEquals(TRAFFIC, actualTrafficResponse);
    }

    private RestfulService getServiceProxy() {
        RecordedResponsePlayback playback = new RecordedResponsePlayback(responseStorage);
        return playback.getProxy();
    }

    private Object[] getArgs(String path){
        Object[] args = new Object[2];
        args[0] = path;
        args[1] = new HashMap<String, String>();
        return args;
    }
}
