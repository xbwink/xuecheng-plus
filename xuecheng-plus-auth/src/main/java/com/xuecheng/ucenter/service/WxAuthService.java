package com.xuecheng.ucenter.service;

import com.xuecheng.ucenter.model.po.XcUser;

/**
 * @author xb
 * @description 微信认证接口
 * @create 2023-06-14 14:29
 * @vesion 1.0
 */
public interface WxAuthService {

    /**
     * 通过code请求微信申请令牌，拿到令牌查询用户信息，将用户信息写入本项目数据库
     * @param code
     * @return
     */
    public XcUser wxAuth(String code);

}
