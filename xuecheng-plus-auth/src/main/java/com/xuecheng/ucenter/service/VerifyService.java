package com.xuecheng.ucenter.service;

import com.xuecheng.ucenter.model.dto.FindPwdDto;
import com.xuecheng.ucenter.model.dto.RegisterDto;

/**
 * @author xb
 * @description 找回密码Service
 * @create 2023-06-26 15:28
 * @vesion 1.0
 */
public interface VerifyService {
    void findPassword(FindPwdDto findPwdDto);
    void register(RegisterDto registerDto);
    boolean verify(String email,String code);
}
