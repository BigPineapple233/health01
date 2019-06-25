package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.PermissionDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ：HWG
 * @date ：Created in 2019/6/24 13:09
 * @description ：
 * @version: 1.0
 */

@Service(interfaceClass = PermissionService.class)
@Transactional
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    PermissionDao permissionDao;


    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<CheckItem> page =  permissionDao.queryPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal() , page.getResult());
    }

    @Override
    public void add(Permission permission) {
        permissionDao.add(permission);
    }

    @Override
    public Permission findById(Integer id) {
        return permissionDao.findById(id);
    }

    @Override
    public void edit(Permission permission) {
        permissionDao.edit(permission);
    }

    @Override
    public void delById(Integer id) {
        //1. 判断检查项是否被检查组关联
        int count = permissionDao.findByCheckItemId(id);
        if(count > 0){
            //2. 如果关联， 不能删除
            throw new RuntimeException("该权限项被角色关联，不能删除！！");
        }
        else{
            //3. 如果不关联，可以删除
            permissionDao.delById(id);
        }
    }

    @Override
    public List<Permission> findAll() {
        return permissionDao.findAll();
    }
}
