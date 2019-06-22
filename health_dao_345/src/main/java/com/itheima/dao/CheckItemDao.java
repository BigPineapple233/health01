package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;

import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public interface CheckItemDao {
    /**
     * 添加
     * @param checkItem
     */
    void add(CheckItem checkItem);

    Page<CheckItem> queryPage(String queryString);

    int findByCheckItemId(Integer id);

    void delById(Integer id);

    CheckItem findById(Integer id);

    void edit(CheckItem checkItem);

    List<CheckItem> findAll();

    /**
     * 根据检查组id查询检查项集合
     * @param checkGroupId
     * @return
     */
    List<CheckItem> findCheckItemListByCheckGroupId(Integer checkGroupId);
}
