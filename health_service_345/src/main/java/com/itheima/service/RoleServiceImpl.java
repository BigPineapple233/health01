package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.RoleDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ：HWG
 * @date ：Created in 2019/6/24 17:03
 * @description ：
 * @version: 1.0
 */
@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleDao roleDao;

    @Override
    public void add(Integer[] permissionIds, Integer[] menuIds, Role role) {
        //1. 添加检查组， 必须拿到检查组的id（mybatis中的主键回显操作）
        roleDao.add(role);
        //2. 维护检查组和检查项的关系
        setRoleAndPermission(role.getId(), permissionIds);
        //2. 维护检查组和检查项的关系
        setRoleAndMenu(role.getId(), menuIds);
    }

    @Override
    public PageResult queryPage(QueryPageBean queryPageBean) {
        //开始分页
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<Role> page = roleDao.findByCondition(queryPageBean.getQueryString());
        return new PageResult(page.getTotal() , page);
    }

    @Override
    public Role findById(Integer id) {
        return roleDao.findById(id);
    }

    @Override
    public List<Integer> findPermissionIdsById(Integer id) {
        return roleDao.findPermissionIdsById(id);
    }

    @Override
    public List<Integer> findMenuIdsById(Integer id) {
        return roleDao.findMenuIdsById(id);
    }

    @Override
    public void edit(Role role, Integer[] permissionIds, Integer[] menuIds) {
        //1. 修改检查组
        roleDao.edit(role);
//        2. 删除该检查组与检查项的关系
        roleDao.deleteAssociation4Permission(role.getId());
        roleDao.deleteAssociation4Menu(role.getId());
//        3. 维护新的关系
        this.setRoleAndPermission(role.getId(),permissionIds);
        this.setRoleAndMenu(role.getId(),menuIds);
    }

    @Override
    public void delById(Integer id) {
        roleDao.deleteAssociation4Permission(id);
        roleDao.deleteAssociation4Menu(id);

        int count = roleDao.findCountById4Permission(id);
        int count2 = roleDao.findCountById4Menu(id);
        if (count > 0 || count2 > 0) {
            throw new RuntimeException("该角色被权限或菜单关联，不能删除！！");
        }
        //
        roleDao.delById(id);
    }

    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }

    /**
     * 维护检查组和检查项的关系
     */
    private void setRoleAndPermission(Integer roleId, Integer[] permissionIds){
        if(roleId != null && permissionIds != null && permissionIds.length > 0){
            for (Integer permissionId : permissionIds) {
                roleDao.insert4permission(roleId,permissionId);
            }
        }
    }

    /**
     * 维护检查组和检查项的关系
     */
    private void setRoleAndMenu(Integer roleId, Integer[] menuIds){
        if(roleId != null && menuIds != null && menuIds.length > 0){
            for (Integer menuId : menuIds) {
                roleDao.insert4menu(roleId,menuId);
            }
        }
    }
}
