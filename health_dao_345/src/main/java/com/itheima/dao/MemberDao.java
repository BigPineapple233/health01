package com.itheima.dao;

import com.itheima.pojo.Member;

import java.util.List;
import java.util.Map;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public interface MemberDao {
    Member findByPhoneNumber(String telephone);

    void reg(Member member);

    int findMemberCountByBeforeMonth(String month);

    /**
     * 新增会员数
     * @param todayDate
     * @return
     */
    long findNewMemberCount(String todayDate);

    /**
     * 总会员数
     * @return
     */
    long findTotalCount();

    /**
     * 查询指定日期之后的新增会员数
     * @param date
     * @return
     */
    long findMemberCountByAfterDate(String date);
    /**
     * 会员数量：按照性别区分,
     */
    List<Map<String, String>> findByMemberCount();

    List<Map<String, String>> findByMemberbirthday();
}
