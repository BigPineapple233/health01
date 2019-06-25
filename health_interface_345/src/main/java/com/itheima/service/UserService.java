package com.itheima.service;

import com.itheima.pojo.SysUser;

import java.util.List;
import java.util.Map;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public interface UserService {
    SysUser findByUsername(String username);

    List<Map<String, Object>> getMenuList(String username);
}
