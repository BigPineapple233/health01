package com.itheima.job;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.RedisConstant;
import com.itheima.service.OrderSettingService;
import com.itheima.utils.DateUtils;
import com.itheima.utils.QiniuUtils;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@RestController
public class ClearImgJob {
    @Reference
    OrderSettingService orderSettingService;

    JedisPool jedisPool;

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    /**
     * 清理图片的方法
     *   1. 比较两个set集合差值，返回值就是垃圾图片的名称的集合
     *   2. 遍历删除七牛云垃圾图片
     *   3. 删除redis 集合中的垃圾图片名称
     */
    public void clear(){
        //获取垃圾图片的名称集合
        Set<String> sdiff = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        //遍历删除
        for (String imgName : sdiff) {
            QiniuUtils.deleteFileFromQiniu(imgName);
            //除redis 集合中的垃圾图片名称
            //srem： 删除redis某set集合中的某个值
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES, imgName);
            System.out.println(imgName);
        }
    }

    /**
     * 定义清除OrderSetting预约设置的方法
     */

    public void clearOrderSetting(){
        //获取当前月份
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        Date today = DateUtils.getToday();
        String format = simpleDateFormat.format(today);
        String startMonth = format+"-1";
        String endMonth = format+"-31";
        //清除当前月份的OrderSetting预约设置
        orderSettingService.clearOrderSetting(startMonth,endMonth);
    }

}
