package com.itheima.dao;

import com.itheima.pojo.Permission;

import java.util.Set;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public interface PermissionDao {

    public Set<Permission> findPermissionsByRoleId(Integer roleId);
}
