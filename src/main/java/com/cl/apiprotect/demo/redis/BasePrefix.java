package com.cl.apiprotect.demo.redis;

/***
 * @ClassName: BasePrefix
 * @Description:TODO
 * @author: chenling
 * @Date: 2019/4/13 22:54
 * @version : V1.0.0
 */
public class BasePrefix implements  KeyPrefix {

    private int expireSeconds;

    private String prefix;

    public BasePrefix(String prefix) {//0代表永不过期
        this(0, prefix);
    }

    public BasePrefix( int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    @Override
    public int expireSeconds() {//默认0代表永不过期
        return expireSeconds;
    }
    @Override
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className+":" + prefix;
    }
}
