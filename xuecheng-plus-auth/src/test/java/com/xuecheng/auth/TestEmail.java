package com.xuecheng.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author xb
 * @description TODO
 * @create 2023-06-26 16:48
 * @vesion 1.0
 */
@SpringBootTest
public class TestEmail {

    @Autowired
    private RedisTemplate redisTemplate;


    @Test
    public void set(){
        redisTemplate.opsForValue().set("key1","hello",2, TimeUnit.MINUTES);

    }

    @Test
    public void get(){
        Object key1 = redisTemplate.opsForValue().get("752247230@qq.com");
        System.out.println(key1);
    }

}
