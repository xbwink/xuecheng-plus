package com.xuecheng.ucenter.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author xb
 * @description 验证码服务远程接口
 * @create 2023-06-13 16:33
 * @vesion 1.0
 */
@FeignClient(value = "checkcode",fallbackFactory = CheckCodeClientFactory .class)
//@RequestMapping("/checkcode")
public interface CheckCodeClient {

    @PostMapping(value = "/checkcode/verify")
    public Boolean verify(@RequestParam("key") String key, @RequestParam("code") String code);

}
