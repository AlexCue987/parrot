package com.parrot.example;

import com.google.gson.Gson;
import com.parrot.RestfulService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class SampleRestfulService implements RestfulService {
    public static final String MATH_DIVIDE = "math/divide";
    public static final String STRINGS_CONCATENATE = "strings/concatenate";
    private final Gson gson = new Gson();

    @Override
    public String get(String path, Map<String, String> parameters) {
        switch (path){
            case MATH_DIVIDE:
                return getResponseJson(parameters, this::divide);
            case STRINGS_CONCATENATE:
                return getResponseJson(parameters, this::concatenate);
            default:
                throw new RuntimeException("Path not implemented: " + path);
        }
    }

    @Override
    public String post(String path, String payload) {
        return null;
    }

    String concatenate(Map<String, String> parameters){
        return parameters.get("token1") + "-" + parameters.get("token2");
    }

    String divide(Map<String, String> parameters){
        BigDecimal numerator = new BigDecimal(parameters.get("numerator"));
        BigDecimal denominator = new BigDecimal(parameters.get("denominator"));
        BigDecimal quotient = numerator.divide(denominator);
        return quotient.toString();
    }

    String getResponseJson(Map<String, String> parameters, ParametersHandler handler){
        Map<String, Object> response = getResponse(parameters, handler);
        return gson.toJson(response);
    }

    Map<String, Object> getResponse(Map<String, String> parameters, ParametersHandler handler){
        try{
            String result = handler.handle(parameters);
            return getSuccessResponse(result);
        }catch (Exception e){
            e.printStackTrace();
            return getErrorResponse(e);
        }
    }

    Map<String, Object> getSuccessResponse(String result){
        Map<String, Object> res = new HashMap<>(2);
        res.put("code", "200");
        res.put("result", result);
        return res;
    }

    Map<String, Object> getErrorResponse(Exception e){
        Map<String, Object> res = new HashMap<>(2);
        res.put("code", "400");
        res.put("error", e.getMessage());
        return res;
    }

    @FunctionalInterface
    private interface ParametersHandler{
        String handle(Map<String, String> parameters);
    }
}
