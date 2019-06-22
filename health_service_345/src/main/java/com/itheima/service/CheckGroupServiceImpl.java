package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    CheckGroupDao checkGroupDao;

    @Override
    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    public void add(Integer[] checkitemIds, CheckGroup checkGroup) {
        //1. 添加检查组， 必须拿到检查组的id（mybatis中的主键回显操作）
        checkGroupDao.add(checkGroup);
        //2. 维护检查组和检查项的关系
        setCheckGroupAndCheckItem(checkGroup.getId(), checkitemIds);
    }

    @Override
    public PageResult queryPage(QueryPageBean queryPageBean) {
        //开始分页
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<CheckGroup> page = checkGroupDao.findByCondition(queryPageBean.getQueryString());
        return new PageResult(page.getTotal() , page);
    }

    @Override
    @Transactional(readOnly = true,propagation = Propagation.SUPPORTS)
    public CheckGroup findById(Integer id) {

        return checkGroupDao.findById(id);
    }

    /**
     * 查询中间表
     * @param id
     * @return
     */
    @Override
    public List<Integer> findCheckItemIdsById(Integer id) {

        return checkGroupDao.findCheckItemIdsById(id);
    }

    /**
     * 编辑提交表单
     *  1. 修改检查组
     *  2. 删除该检查组与检查项的关系
     *  3. 维护新的关系
     * @param checkGroup
     * @param checkitemIds
     */
    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
//        1. 修改检查组
        checkGroupDao.edit(checkGroup);
//        2. 删除该检查组与检查项的关系
        checkGroupDao.deleteAssociation(checkGroup.getId());
//        3. 维护新的关系
        this.setCheckGroupAndCheckItem(checkGroup.getId(),checkitemIds);
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
        checkGroupDao.deleteAssociation(id);

        int count = checkGroupDao.findCountById(id);
        if(count > 0){
            throw new RuntimeException("该检查组被套餐关联，不能删除！！");
        }
        //
        checkGroupDao.delById(id);
    }

    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

    /**
     * 维护检查组和检查项的关系
     */
    private void setCheckGroupAndCheckItem(Integer checkGroupId, Integer[] checkitemIds){
        if(checkGroupId != null && checkitemIds != null && checkitemIds.length > 0){
            for (Integer checkitemId : checkitemIds) {
                checkGroupDao.insert(checkGroupId,checkitemId);
            }
        }
    }


}
