package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.SysUser;
import com.itheima.service.CheckGroupService;
import com.itheima.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Reference
    UserService userService;

    @RequestMapping("/add")
    public Result add(@RequestBody Map<String, Object> map){
        //从map集合中获取jsonArray
        JSONArray jsonArray = (JSONArray) map.get("roleIds");
        //把jsonArray反序列化为整数类型的数组
        Integer[] roleIds = jsonArray.toArray(new Integer[]{});
//        System.out.println(Arrays.toString(checkitemIds));
        //从map集合中获取json对象
        JSONObject jsonObject = (JSONObject) map.get("user");
        //把json对象反序列化为检查组
        SysUser sysUser = jsonObject.toJavaObject(SysUser.class);
//        System.out.println(checkGroup);

        try {
            userService.add(roleIds,sysUser);
            return new Result(true, MessageConstant.ADD_USER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_USER_FAIL);
        }
    }

    @RequestMapping("/findByPage")
    public PageResult findByPage(@RequestBody QueryPageBean queryPageBean){
        return userService.queryPage(queryPageBean);
    }

    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            SysUser sysUser = userService.findById(id);
            return new Result(true,MessageConstant.QUERY_USER_SUCCESS,sysUser);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_USER_FAIL);
        }
    }

    /**
     * 根据检查组id查询关联的检查项id
     * @param id
     * @return
     */
    @RequestMapping("/findRoleIdsById")
    public Result findRoleIdsById(Integer id){
        try {
            List<Integer> roleIds = userService.findRoleIdsById(id);
            return new Result(true,MessageConstant.QUERY_USER_SUCCESS,roleIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_USER_FAIL);
        }
    }

    @RequestMapping("/edit")
    public Result edit(Integer[] roleIds, @RequestBody SysUser sysUser){
        try {
            userService.edit(sysUser,roleIds);
            return new Result(true,MessageConstant.EDIT_USER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_USER_FAIL);
        }
    }

    @RequestMapping("/delById")
    public Result delById(Integer id){
        try {
            userService.delById(id);
            return new Result(true,MessageConstant.DELETE_USER_SUCCESS);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false,e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_USER_FAIL);
        }
    }

    @RequestMapping("/getUsername")
    public Result getUsername(HttpServletRequest request){
        //方法一（不建议）：getAttributeNames  获取session域中所有的属性名,遍历枚举类型， SPRING_SECURITY_CONTEXT
        //Enumeration attributeNames =  request.getSession().getAttributeNames();
        //方法二：
        //获取安全框架的上下文对象
        SecurityContext securityContext = SecurityContextHolder.getContext();
        // 获取认证对象
        Authentication authentication = securityContext.getAuthentication();
        //principal: 重要信息（User）
        Object principal = authentication.getPrincipal();
        //强制转换为User
        User user = (User) principal;
        //获取Username
        String username = user.getUsername();

        return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, username);
    }
}
