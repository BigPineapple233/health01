package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.pojo.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
}
