package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Role;

import java.util.List;
import java.util.Set;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public interface RoleDao {

    public Set<Role> findRoleListByUserId(Integer userId);

    void add(Role role);

    void insert4permission(Integer roleId, Integer permissionId);

    void insert4menu(Integer roleId, Integer menuId);

    Page<Role> findByCondition(String queryString);

    Role findById(Integer id);

    List<Integer> findMenuIdsById(Integer id);

    List<Integer> findPermissionIdsById(Integer id);

    void edit(Role role);

    void deleteAssociation4Permission(Integer id);

    void deleteAssociation4Menu(Integer id);

    int findCountById4Permission(Integer id);

    int findCountById4Menu(Integer id);

    void delById(Integer id);

    List<Role> findAll();
}
