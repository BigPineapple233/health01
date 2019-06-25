package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.pojo.Member;
import org.hamcrest.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    MemberDao memberDao;

    @Override
    public Member findByTelephone(String telephone) {

        return memberDao.findByPhoneNumber(telephone);
    }

    @Override
    public void reg(Member member) {
        memberDao.reg(member);
    }

    /**
     * 每月会员数量的变化
     * @param months
     * @return
     */
    @Override
    public List<Integer>  getReportMemberCount(List<String> months) {
        List<Integer> memberCount  = new ArrayList<>();
        //2018-06-31
        for (String month : months) {
            month += "-31"; // month = month + "-31"
            //查询该月份之前的会员数量
            int count = memberDao.findMemberCountByBeforeMonth(month);
            memberCount.add(count);
        }
        return memberCount;
    }
    /**
     * 会员数量：按照性别区分
     */
    @Override
    public List<Map<String, String>> findByMemberCount() {
        List<Map<String, String>> byMemberCount = memberDao.findByMemberCount();
        for (Map<String, String> stringStringMap : byMemberCount) {
            String name = stringStringMap.get("name");
            if ("1".equals(name)) {
                stringStringMap.put("name", "男");
            } else {
                stringStringMap.put("name", "女");
            }
        }
        return byMemberCount;
    }

    @Override
    public List<Map<String, String>> findByMemberbirthday() {
        return memberDao.findByMemberbirthday();
    }

}
