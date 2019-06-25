package com.itheima.dao;

import com.itheima.pojo.Menu;
import com.itheima.pojo.SysUser;

import java.util.List;
import java.util.Map;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public interface UserDao {
    SysUser findByUsername(String username);

    List<Menu> getMenuList(String username);
}
