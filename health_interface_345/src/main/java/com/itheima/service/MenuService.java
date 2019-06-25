package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Menu;
import com.itheima.pojo.Permission;

import java.util.List;

public interface MenuService {
    void delById(Integer id);

    void edit(Menu menu);

    Menu findById(Integer id);

    PageResult pageQuery(QueryPageBean queryPageBean);

    void add(Menu menu);

    List<Menu> findAll();
}
