package com.itheima.controller;

import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@RestController
@RequestMapping("/login")
public class LoginController {


    @Autowired
    JedisPool jedisPool;

    /**
     * 验证验证码是否一致
     *  1. 获取用户输入的验证码
     *  2. 获取手机号码,获取redis中的验证码
     *  3. 判断验证码是否一致
     * @param map
     * @return
     */
    @RequestMapping("/check")
    public Result check(@RequestBody Map<String,String> map){
//        1. 获取用户输入的验证码
        String validateCode = map.get("validateCode");
//        2. 获取手机号码,获取redis中的验证码
        String telephone = map.get("telephone");
        String redisValidateCode = jedisPool.getResource().get(RedisMessageConstant.SENDTYPE_LOGIN + "-" + telephone);
//        3. 判断验证码是否一致
        if(validateCode != null && validateCode.equals(redisValidateCode)){
            return new Result(true, MessageConstant.LOGIN_SUCCESS);
        }else{
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }
}
