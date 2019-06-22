package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {


    @Autowired
    OrderSettingDao orderSettingDao;

    @Autowired
    MemberDao memberDao;

    @Autowired
    OrderDao orderDao;

    /**
     * 1. 判断该日期是否进行了预约设置,  如果设置，继续，否则，返回错误结果
     * 2. 判断该日期是否预约人数已满， 预约人数大于等于可预约人数，预约已满，返回错误结果
     * 3. 判断是否是会员，如果不是，自动注册为会员；是会员，继续
     * 4. 判断该会员是否在该日期预约了该套餐， 如果预约了，返回错误结果，如果没有预约，继续
     * 5. 无论是否是会员，都需要预约,开始预约
     * 6. 更新预约的人数
     *
     * @param map
     * @return
     */
    @Override
    public Result addOrder(Map<String, Object> map) {
        //获取日期
        Object orderDateObj = map.get("orderDate");
        String orderDateStr = String.valueOf(orderDateObj);
        Date orderDate = null;
        try {
            orderDate = DateUtils.parseString2Date(orderDateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //获取手机号码
        Object telephoneObj = map.get("telephone");
        String telephone = String.valueOf(telephoneObj);
        //获取套餐id
        Object setmealIdObj = map.get("setmealId");
        Integer setmealId = Integer.parseInt(String.valueOf(setmealIdObj));
        //1. 判断该日期是否进行了预约设置,  如果设置，继续，否则，返回错误结果
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(orderDate);
        if(orderSetting == null){
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //2. 判断该日期是否预约人数已满， 预约人数大于等于可预约人数，预约已满，返回错误结果
        if(orderSetting.getReservations() >= orderSetting.getNumber() ){
            return new Result(false,MessageConstant.ORDER_FULL);
        }
        //3. 判断是否是会员，如果不是，自动注册为会员；是会员，继续
        //根据手机号码验证是否是会员
        Member member = memberDao.findByPhoneNumber(telephone);
        if(member == null){
            //不是会员，自动注册
            //创建会员对象
            member = new Member();
            //设置会员信息
            //会员姓名
            String name = map.get("name").toString();
            member.setName(name);
            //会员性别
            member.setSex(map.get("sex").toString());
            //身份证号
            member.setIdCard(String.valueOf(map.get("idCard")));
            //电话号码
            member.setPhoneNumber(telephone);
            //注册时间
            member.setRegTime(new Date());
            //注册会员(需要主键回显)
            memberDao.reg(member);
        }else{
            //是会员，判断是否预约了
            //4. 判断该会员是否在该日期预约了该套餐， 如果预约了，返回错误结果，如果没有预约，继续
            //判断该会员该日期该套餐是否预约
            //会员id
            Integer memberId = member.getId();
            //创建预约条件对象
            Order order = new Order();
            order.setMemberId(memberId);
            order.setSetmealId(setmealId);
            order.setOrderDate(orderDate);
            long count = orderDao.findByCondition(order);
            //判断是否预约
            if(count >0 ){
                //已经预约
                return new Result(false,MessageConstant.HAS_ORDERED);
            }
        }
        //5. 无论是否是会员，都需要预约,开始预约
        //创建预约对象
        Order order = new Order();
        //会员id
        order.setMemberId(member.getId());
        //预约时间'
        order.setOrderDate(orderDate);
        //预约类型
        order.setOrderType(String.valueOf(map.get("orderType")));
        //预约状态
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        //套餐id
        order.setSetmealId(setmealId);

        //添加预约信息
        orderDao.add(order);
        //6. 更新预约的人数
        //预约人数 +1
        orderSetting.setReservations(orderSetting.getReservations() + 1);
        //更新预约设置对象
        orderSettingDao.edit(orderSetting);

        return new Result(true,MessageConstant.ORDER_SUCCESS, order);
    }

    @Override
    public Map<String, Object> findById(Integer id) {
        return orderDao.findById(id);
    }
}
