package com.itheima.service;

import com.itheima.pojo.Member;

import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public interface MemberService {
    Member findByTelephone(String telephone);

    void reg(Member member);

    List<Integer>  getReportMemberCount(List<String> months);
}
