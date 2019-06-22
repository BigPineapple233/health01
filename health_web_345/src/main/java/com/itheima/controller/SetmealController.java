package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.UUID;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    JedisPool jedisPool;

    @Reference
    SetmealService setmealService;

    @RequestMapping("/findByPage")
    public PageResult findByPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = setmealService.findByPage(queryPageBean);
        return pageResult;
    }


    @RequestMapping("/add")
    public Result add(Integer[] checkgroupIds,@RequestBody Setmeal setmeal){
        try {
            setmealService.add(checkgroupIds,setmeal);
            return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }
    }


    @RequestMapping("/upload")
    public Result upload(@RequestBody MultipartFile imgFile){
        try {
            //uuid
            String uuid = UUID.randomUUID().toString().replace("-", "");
            //获取真实的文件名称
            String originalFilename = imgFile.getOriginalFilename();
            //扩展名
            String extendName = originalFilename.substring(originalFilename.lastIndexOf("."));
            //获取唯一的文件名称
            String fileName = uuid + extendName;
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName);

            //把上传成功后的图片名称存在redis中
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

}
