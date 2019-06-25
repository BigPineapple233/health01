package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.UserDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@Service(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public SysUser findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public void add(Integer[] roleIds, SysUser sysUser) {
        //1. 添加检查组， 必须拿到检查组的id（mybatis中的主键回显操作）
        userDao.add(sysUser);
        //2. 维护检查组和检查项的关系
        setUserAndRole(sysUser.getId(), roleIds);
    }

    @Override
    public PageResult queryPage(QueryPageBean queryPageBean) {
        //开始分页
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<SysUser> page = userDao.findByCondition(queryPageBean.getQueryString());
        return new PageResult(page.getTotal() , page);
    }

    @Override
    public SysUser findById(Integer id) {

        return userDao.findById(id);
    }

    /**
     * 查询中间表
     * @param id
     * @return
     */
    @Override
    public List<Integer> findRoleIdsById(Integer id) {

        return userDao.findRoleIdsById(id);
    }

    /**
     * 编辑提交表单
     *  1. 修改检查组
     *  2. 删除该检查组与检查项的关系
     *  3. 维护新的关系
     * @param sysUser
     * @param roleIds
     */
    @Override
    public void edit(SysUser sysUser, Integer[] roleIds) {
//        1. 修改检查组
        userDao.edit(sysUser);
//        2. 删除该检查组与检查项的关系
        userDao.deleteAssociation(sysUser.getId());
//        3. 维护新的关系
        this.setUserAndRole(sysUser.getId(),roleIds);
    }

    /**
     * 如果检查组被套餐关联，不能删除
     *  0. 删除检查项与检查组的关系
     *  1. 判断是否被关联
     *  2. 如果没有管理，删除
     *  3. 如果管理，抛出运行时异常
     * @param id
     */
    @Override
    public void delById(Integer id) {
        userDao.deleteAssociation(id);

        int count = userDao.findCountById(id);
        if(count > 0){
            throw new RuntimeException("该检查组被套餐关联，不能删除！！");
        }
        //
        userDao.delById(id);
    }


    /**
     * 维护检查组和检查项的关系
     */
    private void setUserAndRole(Integer sysUserId, Integer[] roleIds){
        if(sysUserId != null && roleIds != null && roleIds.length > 0){
            for (Integer roleId : roleIds) {
                userDao.insert(sysUserId,roleId);
            }
        }
    }
}
