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
}
