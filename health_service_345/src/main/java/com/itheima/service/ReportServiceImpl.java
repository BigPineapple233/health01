package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.SetmealDao;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {

    @Autowired
    MemberDao memberDao;

    @Autowired
    OrderDao orderDao;

    @Autowired
    SetmealDao setmealDao;

    /**
         reportDate:null,
         todayNewMember :0,
         totalMember :0,
         thisWeekNewMember :0,
         thisMonthNewMember :0,
         todayOrderNumber :0,
         todayVisitsNumber :0,
         thisWeekOrderNumber :0,
         thisWeekVisitsNumber :0,
         thisMonthOrderNumber :0,
         thisMonthVisitsNumber :0,
         hotSetmeal :[
         {name:'阳光爸妈升级肿瘤12项筛查（男女单人）体检套餐',setmeal_count:200,proportion:0.222},
         {name:'阳光爸妈升级肿瘤12项筛查体检套餐',setmeal_count:200,proportion:0.222}
         ]
     * @return
     */
    @Override
    public Map<String, Object> findBusinessReportData() throws Exception {
        //创建统计数据的map集合
        Map<String,Object> resultMap = new HashMap<>();
        //reportDate 统计 时间 -- 今天
        String reportDate = DateUtils.parseDate2String(new Date());
        //获取本周周一的日期
        String thisWeekMonday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        //获取本月1号的日期
        String thisMonthFirstDay = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
        //todayNewMember  今日新增会员数
        long todayNewMember = memberDao.findNewMemberCount(reportDate);
        //totalMember  查询总会员数
        long totalMember = memberDao.findTotalCount();
        //thisWeekNewMember 查询本周新增会员数
        long thisWeekNewMember = memberDao.findMemberCountByAfterDate(thisWeekMonday);
        //thisMonthNewMember :0, 本月新增会员数
        long thisMonthNewMember = memberDao.findMemberCountByAfterDate(thisMonthFirstDay);


        //todayOrderNumber : 今日预约数  - 今天应该到诊数
        long todayOrderNumber = orderDao.findCountByOrderDate(reportDate);
        //todayVisitsNumber  今日到诊数  -- 今天实际到诊数
        long todayVisitsNumber = orderDao.findVisitsByOrderDate(reportDate);
        //thisWeekOrderNumber 本周的预约数
        long thisWeekOrderNumber = orderDao.findCountByAfterOrderDate(thisWeekMonday);
        //thisWeekVisitsNumber :本周的到诊数
        long thisWeekVisitsNumber = orderDao.findVisitsCountByAfterOrderDate(thisWeekMonday);
        //thisMonthOrderNumber :0, 本月的预约数
        long thisMonthOrderNumber = orderDao.findCountByAfterOrderDate(thisMonthFirstDay);
        //thisMonthVisitsNumber :0, 本月的到诊数
        long thisMonthVisitsNumber = orderDao.findVisitsCountByAfterOrderDate(thisMonthFirstDay);

        //hotSetmeal 热门套餐 -- 查询预约最多的套餐 （前三名）
        List<Map<String,Object>> hotSetmeal = setmealDao.findHotSetmeal();



        //添加统计数据到map集合
        resultMap.put("reportDate",reportDate);
        resultMap.put("todayNewMember",todayNewMember);
        resultMap.put("totalMember",totalMember);
        resultMap.put("thisWeekNewMember",thisWeekNewMember);
        resultMap.put("thisMonthNewMember",thisMonthNewMember);
        resultMap.put("todayOrderNumber",todayOrderNumber);
        resultMap.put("todayVisitsNumber",todayVisitsNumber);
        resultMap.put("thisWeekOrderNumber",thisWeekOrderNumber);
        resultMap.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        resultMap.put("thisMonthOrderNumber",thisMonthOrderNumber);
        resultMap.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        resultMap.put("hotSetmeal",hotSetmeal);

        return resultMap;
    }
}
