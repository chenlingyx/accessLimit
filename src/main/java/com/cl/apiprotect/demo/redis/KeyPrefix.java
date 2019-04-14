package com.cl.apiprotect.demo.redis;

public interface KeyPrefix {

    public int expireSeconds();

    public String getPrefix();
}
