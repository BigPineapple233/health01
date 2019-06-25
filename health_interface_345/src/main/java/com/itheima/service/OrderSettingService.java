package com.itheima.service;

import com.itheima.pojo.OrderSetting;

import java.util.List; /**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public interface OrderSettingService {
    void addOrderSettings(List<OrderSetting> orderSettingList);

    List<OrderSetting> findByMonth(String month);

    /**
     * 定义清理OrderSetting的接口
     * @param startMonth
     * @param endMonth
     */

    void clearOrderSetting(String startMonth,String endMonth);
}
