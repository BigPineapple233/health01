package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Reference
    MemberService memberService;


    @Autowired
    JedisPool jedisPool;

    /**
     * 发送验证码
     * 1. 生成验证码
     * 2. 发送验证码到手机号
     * 3. 发送验证码成功，把验证码存储到redis
     * @param telephone
     * @return
     */
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone){
        //1. 生成验证码
        Integer validateCode = ValidateCodeUtils.generateValidateCode(4);
        //2. 发送验证码到手机号
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone, String.valueOf(validateCode));
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //3. 发送验证码成功，把验证码存储到redis
        //验证码需要在5分钟之内有效
        jedisPool.getResource().setex(RedisMessageConstant.SENDTYPE_ORDER+"-"+telephone,5 * 60,String.valueOf(validateCode));
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    /**
     *
     * 为登录发送验证码
     *      1. 生成验证码, 6位
     *      2. 发送验证码到手机
     *      3. 判断是否是会员，如果不是，自动注册为会员
     *      4. 发送成功后，存入redis
     *      5. 存储cookies
     *
     * @param telephone
     * @return
     */
    @RequestMapping("/send4Login")
    public Result send4Login(HttpServletResponse response,String telephone){
        //1. 生成验证码, 6位
        Integer validateCode = ValidateCodeUtils.generateValidateCode(6);
        //2. 发送验证码到手机
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone, validateCode.toString());
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //3. 判断是否是会员，如果不是，自动注册为会员
        //根据手机查询会员信息，如果有信息，说明已注册，没有信息，自动注册
        Member member = memberService.findByTelephone(telephone);
        if(member == null){
            //不是会员，自动注册
            member = new Member();
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            memberService.reg(member);
        }
        //4. 发送成功后，存入redis
        jedisPool.getResource().setex(RedisMessageConstant.SENDTYPE_LOGIN+"-"+telephone, 5 * 60, String.valueOf(validateCode));
        //5. 存储cookies
        //创建cookies
        Cookie cookie = new Cookie("health_login_telephone",telephone);
        //设置cookie的获取范围
        cookie.setPath("/");
        //生存时间
        cookie.setMaxAge(60 * 60 * 24 * 31);
        response.addCookie(cookie);

        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

}
