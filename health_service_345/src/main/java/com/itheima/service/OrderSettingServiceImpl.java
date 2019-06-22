package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    OrderSettingDao orderSettingDao;

    /**
     * 1. 遍历集合添加预约设置对象
     * 2. 查询该日期是否已经预约，如果已经设置预约，执行更新操作， 如果没有设置预约执行添加操作
     * 3. 注意：修改操作时，可预约人数必须大于已预约人数
     * @param orderSettingList
     */
    @Override
    public void addOrderSettings(List<OrderSetting> orderSettingList) {
        if(orderSettingList != null){
            //1. 遍历集合添加预约设置对象
            for (OrderSetting orderSetting : orderSettingList) {
                //查询该日期是否已经预约
                OrderSetting orderSettingDB = findByOrderDate(orderSetting.getOrderDate());
                //如果已经设置预约，执行更新操作， 如果没有设置预约执行添加操作
                if(orderSettingDB != null){
                    //修改操作时，可预约人数必须大于已预约人数
                    //orderSetting.getNumber() 可预约的人数
                    //orderSettingDB.getReservations() 已预约的人数
                    if(orderSetting.getNumber() >= orderSettingDB.getReservations()){
                        edit(orderSetting);
                    }else{
                        throw  new RuntimeException("可预约人数必须大于等于已预约人数");
                    }
                }else{
                    add(orderSetting);
                }
            }
        }
    }

    /**
     * 查询该月所有的预约信息
     *      1号 ---  31号
     *
     *      month：2019-6
     * @param month
     * @return
     */
    @Override
    public List<OrderSetting> findByMonth(String month) {
        String beginDate = month + "-1";  // 2019-6-1
        String endDate = month + "-31"; //2019-6-31
        List<OrderSetting> orderSettingList = orderSettingDao.findByMonth(beginDate, endDate);
        return orderSettingList;
    }

    public OrderSetting findByOrderDate(Date orderDate){
        return orderSettingDao.findByOrderDate(orderDate);
    }

    public void edit(OrderSetting orderSetting){
        orderSettingDao.edit(orderSetting);
    }

    public void add(OrderSetting orderSetting){
        orderSettingDao.add(orderSetting);
    }
}
