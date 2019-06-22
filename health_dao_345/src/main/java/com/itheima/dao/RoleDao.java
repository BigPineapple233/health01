package com.itheima.dao;

import com.itheima.pojo.Role;

import java.util.Set;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public interface RoleDao {

    public Set<Role> findRoleListByUserId(Integer userId);
}
