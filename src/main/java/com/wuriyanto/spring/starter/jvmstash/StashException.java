package com.wuriyanto.spring.starter.jvmstash;

public class StashException extends Exception {

    public StashException(String message) {
        super("stash error : " + message);
    }
}
