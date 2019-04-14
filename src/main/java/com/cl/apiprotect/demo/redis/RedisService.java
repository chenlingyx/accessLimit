package com.cl.apiprotect.demo.redis;

/***
 * @ClassName: RedisService
 * @Description:TODO
 * @author: chenling
 * @Date: 2019/4/13 22:28
 * @version : V1.0.0
 */
public interface RedisService {


        <T> T get(KeyPrefix prefix, String key, Class<T> clazz);

        <T> boolean set(KeyPrefix prefix, String key, T value);

        <T> boolean exists(KeyPrefix prefix, String key);

        boolean delete(KeyPrefix prefix, String key);

        <T> Long incr(KeyPrefix prefix, String key);

        <T> Long decr(KeyPrefix prefix, String key);
}