package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;

import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public interface CheckGroupService {
    void add(Integer[] checkitemIds, CheckGroup checkGroup);

    PageResult queryPage(QueryPageBean queryPageBean);

    CheckGroup findById(Integer id);

    List<Integer> findCheckItemIdsById(Integer id);

    void edit(CheckGroup checkGroup, Integer[] checkitemIds);

    void delById(Integer id);

    List<CheckGroup> findAll();
}
