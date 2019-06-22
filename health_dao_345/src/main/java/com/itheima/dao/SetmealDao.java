package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public interface SetmealDao {
    void add(Setmeal setmeal);

    void insert(Integer setMealId, Integer checkgroupId);

    Page<Setmeal> findByCondition(String queryString);

    List<Setmeal> findAll();

    Setmeal findById(Integer id);

    List<Map<String,String>> findSetmealCount();

    /**
     * 查询热门套餐(预约人数最多的前三名)
     * @return
     */
    List<Map<String,Object>> findHotSetmeal();
}
