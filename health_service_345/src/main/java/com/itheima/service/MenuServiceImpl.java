package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.MenuDao;
import com.itheima.dao.PermissionDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Menu;
import com.itheima.pojo.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ：HWG
 * @date ：Created in 2019/6/24 15:49
 * @description ：
 * @version: 1.0
 */
@Service(interfaceClass = MenuService.class)
@Transactional
public class MenuServiceImpl implements MenuService {
    @Autowired
    MenuDao menuDao;


    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<CheckItem> page =  menuDao.queryPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal() , page.getResult());
    }

    @Override
    public void add(Menu menu) {
        menuDao.add(menu);
    }

    @Override
    public List<Menu> findAll() {
        return menuDao.findAll();
    }

    @Override
    public Menu findById(Integer id) {
        return menuDao.findById(id);
    }

    @Override
    public void edit(Menu menu) {
        menuDao.edit(menu);
    }

    @Override
    public void delById(Integer id) {
        //1. 判断检查项是否被检查组关联
        int count = menuDao.findByCheckItemId(id);
        if(count > 0){
            //2. 如果关联， 不能删除
            throw new RuntimeException("该菜单项被角色关联，不能删除！！");
        }
        else{
            //3. 如果不关联，可以删除
            menuDao.delById(id);
        }
    }
}
