package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Permission;

import java.util.List;
import java.util.Set;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public interface PermissionDao {

    Set<Permission> findPermissionsByRoleId(Integer roleId);

    Page<CheckItem> queryPage(String queryString);

    void add(Permission permission);

    Permission findById(Integer id);

    void edit(Permission permission);

    int findByCheckItemId(Integer id);

    void delById(Integer id);

    List<Permission> findAll();
}
