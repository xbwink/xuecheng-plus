package com.xuecheng.checkcode.service;

/**
 * @author xb
 * @description 发送验证码
 * @create 2023-06-26 18:43
 * @vesion 1.0
 */
public interface SendCodeService {
    void sendEmail(String email,String code);
}
