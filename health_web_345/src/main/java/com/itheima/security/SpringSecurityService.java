package com.itheima.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.SysUser;
import com.itheima.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public class SpringSecurityService implements UserDetailsService {

    //需要使用注解扫描
    @Reference
    UserService userService;

    /**
     * 根据用户名载入一个安全框架的用户对象
     * @param username 用户输入的Username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1. 根据用户名（SysUser）查询数据库中用户信息(还需要查询角色和权限的信息)
        SysUser sysUser = userService.findByUsername(username);
        //2. 创建UserDetails对象返回安全框架
        //如果没有查询到用户，返回null
        if(sysUser == null){
            return null;
        }
        //创建权限集合列表
        List<GrantedAuthority> list = new ArrayList<>();
        //创建权限对象(权限名从数据库中获取)
        for (Role role : sysUser.getRoles()) {
            for (Permission permission : role.getPermissions()) {
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(permission.getKeyword());
                //添加到List集合
                list .add(authority);
            }
        }

        UserDetails userDetails = new User(sysUser.getUsername(),sysUser.getPassword(),list);
        return userDetails;
    }
}
