package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {

    @Reference
    CheckGroupService checkGroupService;


//    @RequestMapping("/add")
//    public Result add(Integer[] checkitemIds, @RequestBody CheckGroup checkGroup){
//        System.out.println(Arrays.toString(checkitemIds));
//        System.out.println(checkGroup);
//        return null;
//    }

    @RequestMapping("/add")
    public Result add(@RequestBody Map<String, Object> map){
        //从map集合中获取jsonArray
        JSONArray jsonArray = (JSONArray) map.get("checkitemIds");
        //把jsonArray反序列化为整数类型的数组
        Integer[] checkitemIds = jsonArray.toArray(new Integer[]{});
//        System.out.println(Arrays.toString(checkitemIds));
        //从map集合中获取json对象
        JSONObject jsonObject = (JSONObject) map.get("checkGroup");
        //把json对象反序列化为检查组
        CheckGroup checkGroup = jsonObject.toJavaObject(CheckGroup.class);
//        System.out.println(checkGroup);

        try {
            checkGroupService.add(checkitemIds,checkGroup);
            return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_CHECKGROUP_FAIL);
        }
    }

    @RequestMapping("/findByPage")
    public PageResult findByPage(@RequestBody QueryPageBean queryPageBean){
        return checkGroupService.queryPage(queryPageBean);
    }

    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            CheckGroup checkGroup = checkGroupService.findById(id);
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    /**
     * 根据检查组id查询关联的检查项id
     * @param id
     * @return
     */
    @RequestMapping("/findCheckItemIdsById")
    public Result findCheckItemIdsById(Integer id){
        try {
            List<Integer> checkItemIds = checkGroupService.findCheckItemIdsById(id);
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkItemIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    @RequestMapping("/edit")
    public Result edit(Integer[] checkitemIds, @RequestBody CheckGroup checkGroup){
        try {
            checkGroupService.edit(checkGroup,checkitemIds);
            return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }

    @RequestMapping("/delById")
    public Result delById(Integer id){
        try {
            checkGroupService.delById(id);
            return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false,e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
    }

    /**
     * 查询所有检查组
     * @return
     */
    @RequestMapping("/findAll")
    public Result findAll(){
        List<CheckGroup> checkGroupList = checkGroupService.findAll();
        return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkGroupList);
    }
}
