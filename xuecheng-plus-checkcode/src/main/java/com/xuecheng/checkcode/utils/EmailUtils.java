package com.xuecheng.checkcode.utils;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

/**
 * @author xb
 * @description TODO
 * @create 2023-06-26 18:45
 * @vesion 1.0
 */
public class EmailUtils {

    /**
     * 发送邮件
     * @param email 收件人
     * @param code  验证码
     */
    public static void sendEmail(String email,String code){
        try {
            HtmlEmail em = new HtmlEmail();//创建一个HtmlEmail实例对象
            em.setHostName("smtp.qq.com");//邮箱的SMTP服务器，一般123邮箱的是smtp.123.com,qq邮箱为smtp.qq.com
            em.setCharset("utf-8");//设置发送的字符类型
            em.setFrom("752247230@qq.com","学成在线");//发送人的邮箱为自己的，用户名可以随便填
            em.setAuthentication("752247230@qq.com","vtbpyazhdlhibfhb");//设置发送人到的邮箱和用户名和授权码(授权码是自己设置的)
            em.addTo(email);//设置收件人
            em.setSubject("Kyle's Blog 邮件测试");//设置发送主题
            em.setMsg("尊敬的用户:你好!\n注册验证码为:" + code +"(有效期为一分钟,请勿告知他人)");//设置发送内容
            em.send();//进行发送
        } catch (EmailException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成一个4位随机验证码
     * @return
     */
    public static String generateCode(){
        char[] arrays = new char[4];
        for (int i = 0; i < 4; i++) {
            int num = (int)Math.floor(Math.random()*58)+65;
            if (num>90&&num<97) {
                num=(int)Math.floor(Math.random()*10);
                arrays[i]=Integer.toString(num).charAt(0);
                continue;
            }
            arrays[i]=(char)num;
        }
        return new String(arrays);
    }

}
