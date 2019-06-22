package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    SetmealService setmealService;

    /**
     * 获取所有的套餐
     * @return
     */
    @RequestMapping("/getSetmeal")
    public Result getSetmeal(){
        List<Setmeal> setmealList =setmealService.findAll();
        return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS, setmealList);
    }

    /**
     * 根据id查询
     *   需要返回套餐信息（包含检查组和检查项信息）
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id){
        Setmeal setmeal = setmealService.findById(id);
        return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
    }
}
