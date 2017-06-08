package com.parrot;

public interface ResponseStorage {
    void save(InvocationInfo invocationInfo, String result);
    String read(InvocationInfo invocationInfo);
}
