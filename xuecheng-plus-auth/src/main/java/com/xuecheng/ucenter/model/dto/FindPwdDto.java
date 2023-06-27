package com.xuecheng.ucenter.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xb
 * @description 找回密码DTO
 * @create 2023-06-26 10:40
 * @vesion 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindPwdDto {
    String cellphone;

    String email;

    String checkcodekey;

    String checkcode;

    String password;

    String confirmpwd;

}
