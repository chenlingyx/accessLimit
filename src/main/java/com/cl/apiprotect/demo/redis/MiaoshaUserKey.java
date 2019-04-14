package com.cl.apiprotect.demo.redis;

public class MiaoshaUserKey extends BasePrefix {

    public static final int TOKEN_EXPIRE = 60*10;

    private MiaoshaUserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static MiaoshaUserKey token = new MiaoshaUserKey(TOKEN_EXPIRE, "tk");
    public static MiaoshaUserKey getById = new MiaoshaUserKey(30, "id");
}
