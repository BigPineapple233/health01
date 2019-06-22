package com.itheima.service;

import com.itheima.entity.Result;

import java.util.Map; /**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public interface OrderService {
    Result addOrder(Map<String, Object> map);

    Map<String,Object> findById(Integer id);
}
