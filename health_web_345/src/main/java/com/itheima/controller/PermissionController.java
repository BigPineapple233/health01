package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.Permission;
import com.itheima.service.PermissionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ：HWG
 * @date ：Created in 2019/6/24 11:55
 * @description ：
 * @version: 1.0
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Reference
    PermissionService permissionService;

    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            List<Permission> permissionList = permissionService.findAll();
            return new Result(true,MessageConstant.QUERY_PERMISSION_SUCCESS,permissionList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_PERMISSION_FAIL);
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
            permissionService.delById(id);
            return new Result(true, MessageConstant.DELETE_PERMISSION_SUCCESS);
        }catch (RuntimeException e){
            return new Result(false, e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_PERMISSION_FAIL);
        }
    }

    /**
     * 修改操作
     * @param permission
     * @return
     */
    @RequestMapping("/edit")
    public Result edit(@RequestBody Permission permission){
        permissionService.edit(permission);
        return new Result(true,MessageConstant.EDIT_PERMISSION_SUCCESS);
    }


    /**
     * 根据id查询
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id){
        Permission permission = permissionService.findById(id);
        return new Result(true,MessageConstant.QUERY_PERMISSION_SUCCESS,permission);
    }


    /**
     * 分页查询数据
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findByPage")
    public PageResult findByPage(@RequestBody QueryPageBean queryPageBean) {
        try {
            PageResult pageResult = permissionService.pageQuery(queryPageBean);
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
     * @param permission
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Permission permission){
        try {
            permissionService.add(permission);
            return  new Result(true, MessageConstant.ADD_PERMISSION_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false,MessageConstant.ADD_PERMISSION_FAIL);
        }
    }
}
