package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.SysUser;

import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public interface UserDao {
    SysUser findByUsername(String username);

    void add(SysUser sysUser);

    Page<SysUser> findByCondition(String queryString);

    SysUser findById(Integer id);

    List<Integer> findRoleIdsById(Integer id);

    void edit(SysUser sysUser);

    void deleteAssociation(Integer id);

    int findCountById(Integer id);

    void delById(Integer id);

    void insert(Integer sysUserId, Integer roleId);
}
