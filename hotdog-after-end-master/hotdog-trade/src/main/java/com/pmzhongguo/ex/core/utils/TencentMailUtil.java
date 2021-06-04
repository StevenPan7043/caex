package com.pmzhongguo.ex.core.utils;

import com.pmzhongguo.ex.business.model.NoticeContentBuilder;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.otc.otcenum.MobiInfoTemplateEnum;
import com.pmzhongguo.otc.sms.SmsSendPool;
import com.pmzhongguo.zzextool.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


/**
 * @description: 腾讯企业邮箱
 * @date: 2018-12-10 09:44
 * @author: 十一
 */
public class TencentMailUtil implements Runnable{

    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    private static Properties properties = null;


    /**
     * 测试各种邮箱
     */
    // ok
//    private static String qqEmail = "1473919563@qq.com";
    private static String qqEmail = "295058106@qq.com";
    // ok
    private static String aliEmail = "liueleven@aliyun.com";
    // ok
    private static String tencentEmail = "service01@njs.com";
    // ok
    private static String appleEamil = "iver31995@icloud.com";
    // ok
    private static String googleEmail = "liueleven1997@gmail.com";
    // ok
    private static String _163Email = "liueleven@163.com";

    // 加载
    static {
        properties = PropertiesUtil.loadProperties("spring/mail.properties");
    }

    private String address;

    private String content;

    public TencentMailUtil(String address, String content) {
        this.address = address;
        this.content = content;
    }

    public TencentMailUtil() {
    }

    @Override
    public void run() {
        sendMailInfo(address,content);
    }

    public void sendMailInfo(String toAccount,String format) {
        try {
            // 得到回话对象
            Session session = Session.getInstance(properties);
            // 获取邮件对象
            Message message = new MimeMessage(session);
            // 设置发件人邮箱地址
            message.setFrom(new InternetAddress(properties.getProperty("mail.account"),"CAEX"));
            // 设置收件人邮箱地址
            message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(toAccount)});
            //message.setRecipient(Message.RecipientType.TO, new InternetAddress("xxx@qq.com"));//一个收件人
            // 设置邮件标题
            message.setSubject("【CAEX】");
            // 设置邮件内容
            message.setText(format);
            // 得到邮差对象
            Transport transport = session.getTransport();
            // 连接自己的邮箱账户
            // 密码为QQ邮箱开通的stmp服务后得到的客户端授权码
            transport.connect(properties.getProperty("mail.account"), properties.getProperty("mail.password"));
            // 发送邮件
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("<========== 邮箱发送异常：" + e.toString());
//            return false;
        }
    }

    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) throws Exception {
//        test();
        NoticeContentBuilder parse
                = new NoticeContentBuilder(MobiInfoTemplateEnum.JH_SECURITY_CODE.getType()
                ,"223232"
                ,5);
        if (StringUtil.isNullOrBank(parse.getContent())) {
            return ;
        }
        SmsSendPool.getInstance().send(new TencentMailUtil(aliEmail,parse.getContent()));
    }
}
