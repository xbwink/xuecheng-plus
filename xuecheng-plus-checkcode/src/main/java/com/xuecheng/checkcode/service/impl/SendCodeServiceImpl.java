package com.xuecheng.checkcode.service.impl;

import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.checkcode.service.SendCodeService;
import com.xuecheng.checkcode.utils.EmailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author xb
 * @description TODO
 * @create 2023-06-26 18:44
 * @vesion 1.0
 */
@Service
public class SendCodeServiceImpl implements SendCodeService {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public void sendEmail(String email, String code) {
        //向用户发送验证码
        try {
            EmailUtils.sendEmail(email,code);
        } catch (Exception e) {
            XueChengPlusException.cast("验证码发送失败,请稍后再试");
        }
        //将验证码储存到redis,TTL设置为2分钟
        redisTemplate.opsForValue().set(email, code,2,TimeUnit.MINUTES);
    }

}
