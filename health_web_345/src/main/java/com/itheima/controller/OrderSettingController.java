package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import com.itheima.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {


    @Reference
    OrderSettingService orderSettingService;

    @RequestMapping("/upload")
    public Result upload(@RequestBody MultipartFile excelFile){
        try {
            //1. 解析excel ， 获取预约设置信息
            List<String[]> strList = POIUtils.readExcel(excelFile);
            //2. 把预约设置的信息写入到数据
            List<OrderSetting> orderSettingList = new ArrayList<>();
            //把解析获取的数据，存入到 List<OrderSetting> 集合中
            for (String[] strs : strList) {
                //一个string数据 对应一个 ordersetting
                OrderSetting orderSetting = new OrderSetting();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                orderSetting.setOrderDate(sdf.parse(strs[0]));
                orderSetting.setNumber(Integer.parseInt(strs[1]));
                //把预约设置对象添加到集合中
                orderSettingList.add(orderSetting);
            }
            //把预约设置对象添加到数据库
            orderSettingService.addOrderSettings(orderSettingList);
            //3. 返回页面
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false,e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    /**
     * 根据日期查询预约设置
     * @return
     */
    @RequestMapping("/findByMonth")
    public Result findByMonth(String month){
        //根据月查询预约设置信息
        List<OrderSetting> orderSettingList = orderSettingService.findByMonth(month);
        //把预约设置列表转换为页面需要的格式数据
        List<Map<String,Object>> leftobj = new ArrayList<>();
        for (OrderSetting orderSetting : orderSettingList) {
            //一个预约设置对象对应一个Map集合
            Map<String,Object> map = new HashMap<>();
            int number = orderSetting.getNumber();
            int reservations = orderSetting.getReservations();
            Date orderDate = orderSetting.getOrderDate();
            int date = orderDate.getDate();

            map.put("date", date);
            map.put("number", number);
            map.put("reservations", reservations);

            //把map添加到集合中
            leftobj.add(map);
        }
        return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS, leftobj);
    }

    /**
     * 编辑预约设置
     * @param orderSetting
     * @return
     */
    @RequestMapping("/edit")
    public Result edit(@RequestBody OrderSetting orderSetting){
        try {
            List<OrderSetting> orderSettingList = new ArrayList<>();
            orderSettingList.add(orderSetting);
            orderSettingService.addOrderSettings(orderSettingList);
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false,e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
    }
}
