package com.parrot;

import java.util.Map;

public interface RestfulService {
    String get(String path, Map<String, String> parameters);
    String post(String path, String payload);
}
