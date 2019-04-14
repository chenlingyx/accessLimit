package com.cl.apiprotect.demo.redis;

/***
 * @ClassName: AccessKey
 * @Description:TODO
 * @author: chenling
 * @Date: 2019/4/13 22:53
 * @version : V1.0.0
 */
public class AccessKey extends  BasePrefix {

    private AccessKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static AccessKey withExpire(int expireSeconds) {
        return new AccessKey(expireSeconds, "access");
    }
}
