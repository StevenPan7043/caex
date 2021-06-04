package com.pmzhongguo.ex.business.service.manager;

import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.ex.business.service.MemberService;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.TencentMailUtil;
import com.pmzhongguo.otc.sms.JuheSend;
import com.pmzhongguo.otc.sms.SmsSendPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description: 一定要写注释啊
 * @date: 2019-12-31 22:00
 * @author: 十一
 */
@Service
public class NoticeManager {

    @Autowired
    private MemberService memberService;


    /**
     * 通过用户id发送
     * @param memberId
     * @param content 发送内容
     */
    public void sendByMemberId(Integer memberId,String content) {
        Member member = memberService.getMemberBy(HelpUtils.newHashMap("id", memberId));
        if (member == null) {
            return;
        }
        if (HelpUtils.isMobile(member.getM_name())) {
            sendOfPhone(member.getM_name(),content);
        }else {
            sendOfMail(member.getM_name(),content);
        }
    }

    /**
     * 通过用户 登录账号 发送
     * @param mName
     * @param content 发送内容
     */
    public void sendByMemberByMname(String mName, String content) {
        Member member = memberService.getMemberBy(HelpUtils.newHashMap("m_name", mName));
        if (member == null) {
            return;
        }
        if (HelpUtils.isMobile(member.getM_name())) {
            sendOfPhone(member.getM_name(),content);
        }else {
            sendOfMail(member.getM_name(),content);
        }
    }

    /**
     * 发送邮件
     * @param mail
     * @param content
     */
    public void sendOfMail(String mail, String content) {
        try {
            SmsSendPool.getInstance().send(new TencentMailUtil(mail,content));
        }catch (Exception e) {
            System.out.println("发送邮件异常："+mail + " content: " + content);
            e.printStackTrace();
        }
    }

    /**
     * 发送手机
     * @param phone
     * @param content 发送内容
     */
    public void sendOfPhone(String phone, String content) {
        try {
            SmsSendPool.getInstance().send(new JuheSend(phone,0,content));
        }catch (Exception e) {
            System.out.println("发送手机："+phone + " content: " + content);
            e.printStackTrace();
        }
    }



}
