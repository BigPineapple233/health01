package com.itheima.service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
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
        //从Redis中获取数据
        String setmealList = jedisPool.getResource().hget("setmeal",11 + "");
        //判断数据是否为空，如果为空,则调findAll向Redis中上传数据
        if (setmealList==null || "".equals(setmealList)){
            List<Setmeal> setmealList1 = setmealDao.findAll();
            jedisPool.getResource().hset("setmeal",11+"", JSON.toJSON(setmealList1).toString());
            System.out.println("走数据库");
            return setmealDao.findAll();
            //如果不为空,则从Redis中获取数据
        }else {
            String setmeal = jedisPool.getResource().hget("setmeal",11 + "");
            if (!StringUtils.isBlank(setmeal)) {
                //把字符串转换成list
                System.out.println("走Redis");
                return JSON.parseArray(setmeal, Setmeal.class);
            }
        }
        return null;
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
        String setmeal = jedisPool.getResource().get(id+"");
        if (setmeal==null || "".equals(setmeal)){
            Setmeal setmeal1 = setmealDao.findById(id);
            String set = JSON.toJSONString(setmeal1);
            jedisPool.getResource().set(id+"", set);
            System.out.println("走数据库");
            return setmeal1;
        }else {
            String setmeal2 = jedisPool.getResource().get(id+"");
            Setmeal setmeal1 = JSON.parseObject(setmeal2, new TypeReference<Setmeal>(){});
            System.out.println("走Redis");
            return setmeal1;
        }
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
