package com.pmzhongguo.notice.dingding;

import com.pmzhongguo.notice.dto.NoticeDto;
import com.pmzhongguo.notice.service.DingDingNoticeUtil;
import com.pmzhongguo.notice.support.NoticeConstant;

/**
 * @description: 钉钉发送测试
 * @date: 2020-02-21 10:23
 * @author: 十一
 */
public class DingDingNoticeTest {

    public static void main(String[] args) {
        NoticeDto noticeDto = NoticeDto.builder()
                .content(String.format(NoticeConstant.WITHDRAW_NOTICE_MSG + " 测试消息，请忽略！", "ETH"))
                .subject(NoticeConstant.WITHDRAW_NOTICE_SUBJECT)
                .build();
        DingDingNoticeUtil.send(noticeDto);
    }
}
