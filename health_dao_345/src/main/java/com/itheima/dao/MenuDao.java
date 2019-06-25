package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Menu;
import com.itheima.pojo.Permission;

import java.util.List;

public interface MenuDao {
    Page<CheckItem> queryPage(String queryString);

    void add(Menu menu);

    Menu findById(Integer id);

    void edit(Menu menu);

    int findByCheckItemId(Integer id);

    void delById(Integer id);

    List<Menu> findAll();
}
