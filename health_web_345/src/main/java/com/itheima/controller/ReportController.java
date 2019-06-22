package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.MemberService;
import com.itheima.service.ReportService;
import com.itheima.service.SetmealService;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    MemberService memberService;

    @Reference
    SetmealService setmealService;

    @Reference
    ReportService reportService;

    /**
     * 1. 获取运营数据
     * 2. 获取excel模板对象
     * 3. 把运营数据存储到excel指定的位置
     * 4. 响应用户
     * @return
     */
    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        try {
            //1. 获取运营数据
            Map<String, Object> businessReportData = reportService.findBusinessReportData();
            //2. 获取excel模板对象
            //获取模板的真实路径
            String excelPath = request.getSession().getServletContext().getRealPath("/template")+ File.separator+"report_template.xlsx";
            //获取模板excle的流对象
            FileInputStream inputStream = new FileInputStream(new File(excelPath));
            //创建excel模板的工作薄对象
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            //3. 把运营数据存储到excel指定的位置
            //获取第一个工作表
            XSSFSheet sheet = workbook.getSheet("Sheet1");
            //获取工作表的某一行
            XSSFRow row = sheet.getRow(2);
            //获取某单元格
            XSSFCell cell = row.getCell(5);
            //获取导出运营数据的日期
            Object reportDate = businessReportData.get("reportDate");
            //给单元格赋值
            cell.setCellValue(String.valueOf(reportDate));

            //获取工作表的某一行
            row = sheet.getRow(4);
            //获取某单元格
            cell = row.getCell(5);
            //获取今日新增会员
            Object todayNewMember = businessReportData.get("todayNewMember");
            //给单元格赋值
            cell.setCellValue(String.valueOf(todayNewMember));

            //获取工作表的某一行
            row = sheet.getRow(4);
            //获取某单元格
            cell = row.getCell(7);
            //获取总会员数
            Object totalMember = businessReportData.get("totalMember");
            //给单元格赋值
            cell.setCellValue(String.valueOf(totalMember));

            //获取工作表的某一行
            row = sheet.getRow(5);
            //获取某单元格
            cell = row.getCell(5);
            //获取本周新增会员
            Object thisWeekNewMember = businessReportData.get("thisWeekNewMember");
            //给单元格赋值
            cell.setCellValue(String.valueOf(thisWeekNewMember));

            //获取工作表的某一行
            row = sheet.getRow(5);
            //获取某单元格
            cell = row.getCell(7);
            //获取本月新增会员数
            Object thisMonthNewMember = businessReportData.get("thisMonthNewMember");
            //给单元格赋值
            cell.setCellValue(String.valueOf(thisMonthNewMember));

            //获取工作表的某一行
            row = sheet.getRow(7);
            //获取某单元格
            cell = row.getCell(5);
            //获取今日预约数
            Object todayOrderNumber = businessReportData.get("todayOrderNumber");
            //给单元格赋值
            cell.setCellValue(String.valueOf(todayOrderNumber));

            //获取工作表的某一行
            row = sheet.getRow(7);
            //获取某单元格
            cell = row.getCell(7);
            //获取今日到诊数
            Object todayVisitsNumber = businessReportData.get("todayVisitsNumber");
            //给单元格赋值
            cell.setCellValue(String.valueOf(todayVisitsNumber));

            //获取工作表的某一行
            row = sheet.getRow(8);
            //获取某单元格
            cell = row.getCell(5);
            //获取本周预约数
            Object thisWeekOrderNumber = businessReportData.get("thisWeekOrderNumber");
            //给单元格赋值
            cell.setCellValue(String.valueOf(thisWeekOrderNumber));

            //获取工作表的某一行
            row = sheet.getRow(8);
            //获取某单元格
            cell = row.getCell(7);
            //获取本周到诊数
            Object thisWeekVisitsNumber = businessReportData.get("thisWeekVisitsNumber");
            //给单元格赋值
            cell.setCellValue(String.valueOf(thisWeekVisitsNumber));

            //获取工作表的某一行
            row = sheet.getRow(9);
            //获取某单元格
            cell = row.getCell(5);
            //获取本月预约数
            Object thisMonthOrderNumber = businessReportData.get("thisMonthOrderNumber");
            //给单元格赋值
            cell.setCellValue(String.valueOf(thisMonthOrderNumber));

            //获取工作表的某一行
            row = sheet.getRow(9);
            //获取某单元格
            cell = row.getCell(7);
            //获取本月到诊数
            Object thisMonthVisitsNumber = businessReportData.get("thisMonthVisitsNumber");
            //给单元格赋值
            cell.setCellValue(String.valueOf(thisMonthVisitsNumber));


            //给热门套餐导出数据
            //获取热门套餐
            List<Map<String,Object>> hotSemeal = (List<Map<String, Object>>) businessReportData.get("hotSetmeal");
            int rowNum = 12;
            for (Map<String, Object> setmeal : hotSemeal) {
                row = sheet.getRow(rowNum);
                //获取套餐名称的单元格
                cell = row.getCell(4);
                cell.setCellValue(String.valueOf(setmeal.get("name")));
                //获取套餐名称的单元格
                cell = row.getCell(5);
                cell.setCellValue(String.valueOf(setmeal.get("setmeal_count")));
                //获取套餐名称的单元格
                cell = row.getCell(6);
                cell.setCellValue(String.valueOf(setmeal.get("proportion")));
                //行号 +1
                rowNum ++ ;
            }
            //4. 响应用户
            //通过输出流进行文件下载
            ServletOutputStream out = response.getOutputStream();
            //响应的类型为excel
            response.setContentType("application/vnd.ms-excel");
            //attachment --- 作为附件下载
            //filename -- 指定文件名
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            workbook.write(out);

            out.flush();
            out.close();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
        return  null;
    }


    /**
     * 查询运营数据
     * @return
     */
    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
        try {
            Map<String, Object> map = reportService.findBusinessReportData();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }



    /**
     * 查询套餐统计数据
     *  Map<String,Object>
     *         setmealNames: ['一般体检'，'入职体检','其他套餐'] ==>  List<String>
     *         setmealCount:
     *          [{name:'一般体检',value: 20},{name:'一般体检',value: 20}]  ==> List<Map<String,String>>
     * @return
     */
    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport(){
        try {
            List<Map<String,String>>  setmealCount = setmealService.findSetmealCount();

            Map<String, Object> map = new HashMap<>();
            //把所有的套餐名称存储到一个list集合中
            List<String> setmealNames = new ArrayList<>();
            for (Map<String, String> setmeal : setmealCount) {
                String name = setmeal.get("name");
                setmealNames.add(name);
            }

            map.put("setmealCount",setmealCount);
            map.put("setmealNames",setmealNames);

            return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, map );
        } catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }






//    public static void main(String[] args) {
//        Calendar calendar = Calendar.getInstance();
//        //向前退了 12个月
//        calendar.add(Calendar.MONTH, -12);
//        List<String> months = new ArrayList<>();
//        for (int i = 0; i < 12 ; i++) {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
//            String format = sdf.format(calendar.getTime());
//            months.add(format);
//            calendar.add(Calendar.MONTH, 1);
//        }
//        System.out.println(months);
//    }


    /**
     * 统计会员数量
     *
     * 需要什么样的数据
     *      一年内的月份
     *      一年内每月的数据统计
     *  Map<String,Object>
     *         months:一年内的月份
     *         memberCount:一年内每月的数据统计
     *
     * @return
     */
    @RequestMapping("/getMemberReport")
    public Result getMemberReport(){

        Map<String,Object> map = new HashMap<>();

        Calendar calendar = Calendar.getInstance();
        //向前退了 12个月
        calendar.add(Calendar.MONTH, -12);
        List<String> months = new ArrayList<>();
        for (int i = 0; i < 12 ; i++) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            String format = sdf.format(calendar.getTime());
            months.add(format);
            calendar.add(Calendar.MONTH, 1);
        }

        //把需要展示数据的月份添加map中
        map.put("months", months);

        List<Integer> memberCount =memberService.getReportMemberCount(months);
        //把需要展示数据的月份的会员数量添加map中
        map.put("memberCount", memberCount);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
    }


}
