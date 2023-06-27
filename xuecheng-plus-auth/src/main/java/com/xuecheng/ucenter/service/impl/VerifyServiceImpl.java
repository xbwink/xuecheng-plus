package com.xuecheng.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.ucenter.mapper.XcUserMapper;
import com.xuecheng.ucenter.mapper.XcUserRoleMapper;
import com.xuecheng.ucenter.model.dto.FindPwdDto;
import com.xuecheng.ucenter.model.dto.RegisterDto;
import com.xuecheng.ucenter.model.po.XcUser;
import com.xuecheng.ucenter.model.po.XcUserRole;
import com.xuecheng.ucenter.service.AuthService;
import com.xuecheng.ucenter.service.VerifyService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author xb
 * @description TODO
 * @create 2023-06-26 15:29
 * @vesion 1.0
 */
@Service
public class VerifyServiceImpl implements VerifyService {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    XcUserMapper xcUserMapper;

    @Autowired
    XcUserRoleMapper xcUserRoleMapper;

    @Override
    public void findPassword(FindPwdDto findPwdDto) {
        //todo:校验验证码
        boolean verify = verify(findPwdDto.getEmail(), findPwdDto.getCheckcode());
        if(!verify){
            XueChengPlusException.cast("验证码输入错误");
        }
        //2、判断两次密码是否一致，不一致则抛出异常
        String password = findPwdDto.getPassword();
        String confirmpwd = findPwdDto.getConfirmpwd();
        if(!password.equals(confirmpwd)){
            XueChengPlusException.cast("两次密码不一致！");
        }
        //3、根据手机号和邮箱查询用户(这里只实现邮箱)
        LambdaQueryWrapper<XcUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(XcUser::getEmail,findPwdDto.getEmail());
        XcUser xcUser = xcUserMapper.selectOne(wrapper);
        //4、如果找到用户更新为新密码
        if(xcUser==null){
            XueChengPlusException.cast("未找到用户！");
        }
        xcUser.setPassword(new BCryptPasswordEncoder().encode(password));
        xcUserMapper.updateById(xcUser);
    }

    @Transactional
    @Override
    public void register(RegisterDto registerDto) {
        //todo:校验验证码
        boolean verify = verify(registerDto.getEmail(), registerDto.getCheckcode());
        if(!verify){
            XueChengPlusException.cast("验证码输入错误");
        }
        //2、判断两次密码是否一致，不一致则抛出异常
        String password = registerDto.getPassword();
        String confirmpwd = registerDto.getConfirmpwd();
        if(!password.equals(confirmpwd)){
            XueChengPlusException.cast("两次密码不一致！");
        }
        //3、根据手机号和邮箱查询用户(这里只实现邮箱)
        LambdaQueryWrapper<XcUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(XcUser::getEmail,registerDto.getEmail());
        XcUser xcUser = xcUserMapper.selectOne(wrapper);
        if(xcUser!=null){
            XueChengPlusException.cast("该用户已经存在！");
        }
        //4、向用户表、用户角色关系表添加数据。角色为学生角色。
        XcUser user = new XcUser();
        BeanUtils.copyProperties(registerDto,user);
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setName(registerDto.getNickname());
        user.setUtype("101001");//设置用户类型位学生
        user.setStatus("1");//设置默认状态
        user.setCreateTime(LocalDateTime.now());
        xcUserMapper.insert(user);

        XcUserRole userRole = new XcUserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId("17");//设置用户角色为学生
        userRole.setCreateTime(LocalDateTime.now());
        xcUserRoleMapper.insert(userRole);
    }

    @Override
    public boolean verify(String email,String code) {
        //从redis获取缓存的验证码
        String rightCode = redisTemplate.opsForValue().get(email);
        //判断与用户输入的是否一致
        if(rightCode.equalsIgnoreCase(code)){
            redisTemplate.delete(email);
            return true;
        }
        return false;
    }

}
