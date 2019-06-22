package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public interface CheckGroupDao {
    /**
     *  添加检查组
     * @param checkGroup
     */
    void add(CheckGroup checkGroup);

    /**
     * 维护检查项和检查组的关系
     * @param checkGroupId
     * @param checkitemId
     */
    void insert(@Param("checkGroupId") Integer checkGroupId, @Param("checkitemId") Integer checkitemId);

    /**
     * 根据查询条件查询检查组
     * @param queryString
     * @return
     */
    Page<CheckGroup> findByCondition(String queryString);

    CheckGroup findById(Integer id);

    List<Integer> findCheckItemIdsById(Integer id);

    void edit(CheckGroup checkGroup);

    void deleteAssociation(Integer id);

    int findCountById(Integer id);

    void delById(Integer id);

    List<CheckGroup> findAll();

    /**
     * 根据套餐id查询检查组列表
     * @param setmealId
     * @return
     */
    List<CheckGroup> findCheckGroupListBySetmealId(Integer setmealId);
}
