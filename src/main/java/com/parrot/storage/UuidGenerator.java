package com.parrot.storage;

import java.util.UUID;

public class UuidGenerator {
    public String get(){
        return UUID.randomUUID().toString();
    }
}
