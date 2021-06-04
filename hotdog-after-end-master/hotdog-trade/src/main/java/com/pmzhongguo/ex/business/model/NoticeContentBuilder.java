package com.pmzhongguo.ex.business.model;

import com.pmzhongguo.ex.business.service.manager.NoticeManager;
import com.pmzhongguo.otc.otcenum.MobiInfoTemplateEnum;

/**
 * @description: 一定要写注释啊
 * @date: 2019-12-31 22:43
 * @author: 十一
 */
public class NoticeContentBuilder {

    private Object[] args;

    private int msgType;

    public NoticeContentBuilder( int msgType,Object ...args) {
        this.args = args;
        this.msgType = msgType;
    }

    public String getContent() {
        MobiInfoTemplateEnum enumByType = MobiInfoTemplateEnum.getEnumByType(msgType);
        if (enumByType == null) {
            return null;
        }
        return String.format(enumByType.getCode(),args);
    }

    public static void main(String[] args) {
        NoticeContentBuilder parse
                = new NoticeContentBuilder(100010
                ,"ETH"
                ,"2019-12-31 23:32:22"
                ,"23.33");
        String content = parse.getContent();
        System.out.println(content);
        new NoticeManager().sendOfPhone("15501458626",content);
    }
}
