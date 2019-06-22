package com.itheima.dao;

import com.itheima.pojo.SysUser;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public interface UserDao {
    SysUser findByUsername(String username);
}
