package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Reference
    UserService userService;

    @RequestMapping("/getUsername")
    public Result getUsername(HttpServletRequest request){
        //方法一（不建议）：getAttributeNames  获取session域中所有的属性名,遍历枚举类型， SPRING_SECURITY_CONTEXT
        //Enumeration attributeNames =  request.getSession().getAttributeNames();
        //方法二：
        //获取安全框架的上下文对象
        SecurityContext securityContext = SecurityContextHolder.getContext();
        // 获取认证对象
        Authentication authentication = securityContext.getAuthentication();
        //principal: 重要信息（User）
        Object principal = authentication.getPrincipal();
        //强制转换为User
        User user = (User) principal;
        //获取Username
        String username = user.getUsername();

        return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, username);
    }
    @RequestMapping("/getMenuList")
    public Result getMenuList(){
        //获取安全框架的上下文对象
        SecurityContext securityContext = SecurityContextHolder.getContext();
        // 获取认证对象
        Authentication authentication = securityContext.getAuthentication();
        //principal: 重要信息（User）
        Object principal = authentication.getPrincipal();
        //强制转换为User
        User user = (User) principal;
        //获取Username
        String username = user.getUsername();

        if (username==null || "".equals(username) ){
            return new Result(false,MessageConstant.GET_MENU_FAIL);
        }
        //创建返回的List集合
        List<Map<String,Object>> menuList=userService.getMenuList(username);
        return new Result(true,MessageConstant.GET_MENU_SUCCESS,menuList);
    }
}
