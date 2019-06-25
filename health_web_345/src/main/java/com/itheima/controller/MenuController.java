package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Menu;
import com.itheima.pojo.Permission;
import com.itheima.service.MenuService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ：HWG
 * @date ：Created in 2019/6/24 15:40
 * @description ：
 * @version: 1.0
 */
@RestController
@RequestMapping("/menu")
public class MenuController {
    @Reference
    MenuService menuService;

    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            List<Menu> menuList = menuService.findAll();
            return new Result(true,MessageConstant.QUERY_MENU_SUCCESS,menuList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_MENU_FAIL);
        }
    }

    /**
     * 根据id删除
     * @param id
     * @return
     */
    @RequestMapping("/delById")
    public Result delById(Integer id){
        try {
            menuService.delById(id);
            return new Result(true, MessageConstant.DELETE_MENU_SUCCESS);
        }catch (RuntimeException e){
            return new Result(false, e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_MENU_FAIL);
        }
    }

    /**
     * 修改操作
     * @param menu
     * @return
     */
    @RequestMapping("/edit")
    public Result edit(@RequestBody Menu menu){
        menuService.edit(menu);
        return new Result(true,MessageConstant.EDIT_MENU_SUCCESS);
    }


    /**
     * 根据id查询
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id){
        Menu menu = menuService.findById(id);
        return new Result(true,MessageConstant.QUERY_MENU_SUCCESS,menu);
    }


    /**
     * 分页查询数据
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findByPage")
    public PageResult findByPage(@RequestBody QueryPageBean queryPageBean) {
        try {
            PageResult pageResult = menuService.pageQuery(queryPageBean);
            return pageResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 添加
     *
     * ctrl + alt + T
     * @param menu
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Menu menu){
        try {
            menuService.add(menu);
            return  new Result(true, MessageConstant.ADD_MENU_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false,MessageConstant.ADD_MENU_FAIL);
        }
    }
}
