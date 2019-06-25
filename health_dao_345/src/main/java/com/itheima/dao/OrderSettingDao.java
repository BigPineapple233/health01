package com.itheima.dao;

import com.itheima.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public interface OrderSettingDao {
    OrderSetting findByOrderDate(Date orderDate);

    void edit(OrderSetting orderSetting);

    void add(OrderSetting orderSetting);

    List<OrderSetting> findByMonth(String beginDate, String endDate);

    /**
     * 在Dao层创建清理Order Setting预约设置的接口
     *
     * @param startMonth 每月开始第一天
     * @param endMonth   每月结束的最后一天
     */

    void clearOrderSetting(@Param("startMonth") String startMonth,@Param("endMonth")  String endMonth);
}
