package com.xuecheng.ucenter.model.dto;

import lombok.Data;

/**
 * @author xb
 * @description TODO
 * @create 2023-06-27 9:29
 * @vesion 1.0
 */
@Data
public class RegisterDto extends FindPwdDto{

    String username;
    String nickname;


}
