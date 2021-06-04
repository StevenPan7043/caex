package com.pmzhongguo.notice.dto;

import lombok.Builder;

/**
 * @description: 消息发送对象
 * @date: 2020-02-20 15:30
 * @author: 十一
 */
@Builder
public class NoticeDto {

    /**
     * 接受账号
     */
    private String[] receiver;

    /**
     * 发送内容
     */
    private String content;

    /**
     * 主题
     */
    private String subject;

    /**
     * 缓存的key
     */
    private String cacheKey;

    public String[] getReceiver() {
        return receiver;
    }

    public void setReceiver(String[] receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }
}
