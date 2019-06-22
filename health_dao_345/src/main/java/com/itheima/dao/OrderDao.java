package com.itheima.dao;

import com.itheima.pojo.Order;

import java.util.Map;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public interface OrderDao {
    long findByCondition(Order order);

    void add(Order order);

    Map<String,Object> findById(Integer id);

    /**
     * 今日应该到诊数
     * @param todayDate
     * @return
     */
    long findCountByOrderDate(String todayDate);

    /**
     * 今日实际到诊数
     * @param todayDate
     * @return
     */
    long findVisitsByOrderDate(String todayDate);

    /**
     * 查询指定日期之后的预约数
     * @param date
     * @return
     */
    long findCountByAfterOrderDate(String date);

    /**
     * 查询指定日期之后的到诊数
     * @param date
     * @return
     */
    long findVisitsCountByAfterOrderDate(String date);
}
