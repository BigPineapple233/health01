package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    JedisPool jedisPool;

    @Autowired
    SetmealDao setmealDao;

    /**
     * 1. 添加套餐数据
     * 2. 维护套餐和检查组的关系
     * @param checkgroupIds
     * @param setmeal
     */
    @Override
    public void add(Integer[] checkgroupIds, Setmeal setmeal) {
        //1. 添加套餐数据(注意：主键回显)
        setmealDao.add(setmeal);
        //2. 维护套餐和检查组的关系
        setSetmealAndCheckGroup(setmeal.getId(),checkgroupIds);
        //把图片名称写入redis
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, setmeal.getImg());
    }

    @Override
    public PageResult findByPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());

        Page<Setmeal> page = setmealDao.findByCondition(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(), page);
    }

    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    /**
     * 方式一：
         * 1. 根据id查询套餐
         * 2. 根据套餐id查询检查组
         * 3. 根据检查组id查询检查项
         * 4. 组合数据
     * 方式二：
     *      根据id查询套餐，在持久层做一对多数据映射
     * @param id
     * @return
     */
    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    @Override
    public List<Map<String, String>> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }

    private void setSetmealAndCheckGroup(Integer setMealId, Integer[] checkgroupIds) {
        if(setMealId != null && checkgroupIds != null && checkgroupIds.length > 0){
            for (Integer checkgroupId : checkgroupIds) {
                setmealDao.insert(setMealId,checkgroupId);
            }
        }
    }
}
