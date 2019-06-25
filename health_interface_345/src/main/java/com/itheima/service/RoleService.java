package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Role;

import java.util.List;

public interface RoleService {
    void add(Integer[] permissionIds, Integer[] menuIds, Role role);

    PageResult queryPage(QueryPageBean queryPageBean);

    Role findById(Integer id);

    List<Integer> findPermissionIdsById(Integer id);

    List<Integer> findMenuIdsById(Integer id);

    void edit(Role role, Integer[] permissionIds, Integer[] menuIds);

    void delById(Integer id);

    List<Role> findAll();
}
