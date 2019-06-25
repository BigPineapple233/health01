package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.UserDao;
import com.itheima.pojo.Menu;
import com.itheima.pojo.SysUser;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@Service(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public SysUser findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public List<Map<String, Object>> getMenuList(String username) {
        //创建返回数据集合
        List<Map<String, Object>> menuList = new ArrayList<>();
        //调用UserDao查询到该用户所有应用菜单
        List<Menu> list = userDao.getMenuList(username);

        //创建一个菜单集合
        List<Menu> menuOneList = new ArrayList<>();
        //创建二级菜单集合
        List<Menu> menuTwoList = new ArrayList<>();
        for (Menu menu : list) {
            //设置一级菜单集合
            Integer parentMenuId = menu.getParentMenuId();
            if (parentMenuId == null) {
                menuOneList.add(menu);
            } else {
                menuTwoList.add(menu);
            }
        }
        for (Menu menuOne : menuOneList) {

            //创建一级菜单Map集合
            Map<String, Object> menuOneMap = new HashMap<>();
            //创建二级菜单集合
            List<Map<String, String>> oneAndTwo = new ArrayList<>();
            String onePath = menuOne.getPath();
            menuOneMap.put("path", onePath);
            String name = menuOne.getName();
            menuOneMap.put("title", name);
            String icon = menuOne.getIcon();
            menuOneMap.put("icon", icon);
            for (Menu menuTwo : menuTwoList) {
                int menuOneId = menuOne.getId();
                int parentMenuId = menuTwo.getParentMenuId();
                if (menuOneId == parentMenuId) {
                    //创建二级菜单map集合
                    Map<String, String> menuTwoMap = new HashMap<>();
                    String twoPath = menuTwo.getPath();
                    menuTwoMap.put("path", twoPath);
                    String name1 = menuTwo.getName();
                    menuTwoMap.put("title", name1);
                    String linkUrl ="/pages/"+ menuTwo.getLinkUrl();
                    menuTwoMap.put("linkUrl", linkUrl);
                    oneAndTwo.add(menuTwoMap);
                }
            }
            menuOneMap.put("children", oneAndTwo);
            menuList.add(menuOneMap);
        }
        System.out.println(menuList);
        return menuList;
    }

}
