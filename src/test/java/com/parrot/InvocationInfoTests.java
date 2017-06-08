package com.parrot;

import org.junit.Assert;
import org.junit.Test;

import java.util.SortedMap;
import java.util.TreeMap;

public class InvocationInfoTests {
    @Test
    public void works(){
        SortedMap<String, String> queryParameters = getDailyDetailedQueryParameters();
        InvocationInfo t = new InvocationInfo("get", "/weather/current/", queryParameters);
        System.out.println(t);
    }

    @Test
    public void printMapAsSortedKeyValuePairs(){
        Assert.assertEquals("frequency=daily;level=detailed",
                InvocationInfo.printMapAsSortedKeyValuePairs(getDailyDetailedQueryParameters()));
    }

    @Test
    public void compareTo_comparesMethodNames(){
        SortedMap<String, String> queryParameters = getDailyDetailedQueryParameters();
        InvocationInfo get = new InvocationInfo("get", "/weather/current/", queryParameters);
        InvocationInfo post = new InvocationInfo("post", "/weather/current/", queryParameters);
        Assert.assertTrue(get.compareTo(post) < 0);
        Assert.assertTrue(post.compareTo(get) > 0);
    }

    @Test
    public void compareTo_comparesQueryParameters(){
        InvocationInfo dailyDetailed = new InvocationInfo("get", "/weather/current/", getDailyDetailedQueryParameters());
        InvocationInfo hourlyBasic = new InvocationInfo("get", "/weather/current/", getHourlyBasicQueryParameters());
        Assert.assertTrue(dailyDetailed.compareTo(hourlyBasic) < 0);
        Assert.assertTrue(hourlyBasic.compareTo(dailyDetailed) > 0);
    }

    private SortedMap<String, String> getDailyDetailedQueryParameters() {
        SortedMap<String, String> queryParameters = new TreeMap<>();
        queryParameters.put("level", "detailed");
        queryParameters.put("frequency", "daily");
        return queryParameters;
    }

    private SortedMap<String, String> getHourlyBasicQueryParameters() {
        SortedMap<String, String> queryParameters = new TreeMap<>();
        queryParameters.put("level", "basic");
        queryParameters.put("frequency", "hourly");
        return queryParameters;
    }
}
