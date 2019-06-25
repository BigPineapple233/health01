package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.SysUser;

import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public interface UserService {
    SysUser findByUsername(String username);

    void add(Integer[] roleIds, SysUser sysUser);

    PageResult queryPage(QueryPageBean queryPageBean);

    SysUser findById(Integer id);

    List<Integer> findRoleIdsById(Integer id);

    void edit(SysUser sysUser, Integer[] roleIds);

    void delById(Integer id);
}
