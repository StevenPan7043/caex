package com.zytx.business.service;

import com.zytx.common.entity.CChatInfo;

import javax.servlet.http.HttpServletRequest;

public interface ChatService {
    /**
     * 管理员禁言
     *
     * @param id      用户ID
     * @param banTime 禁言时间，精确到秒。-1表示永久封禁 0表示解封
     */
    public String banMember(HttpServletRequest request, Integer id, Integer banTime);

    /**
     * 管理管理员
     *
     * @param id   用户ID
     * @param type 操作 0删除管理员 1添加管理员
     */
    public String updateManager(HttpServletRequest request, Integer id, Integer type);

    /**
     * 修改用户信息
     *
     * @param chatInfo
     */
    public String updateInfoById(HttpServletRequest request, CChatInfo chatInfo);

    /**
     * 将禁言用户加载到redis中
     */
    public void initChatInfo();

    /**
     * 处理聊天数
     */
    public void onChatCount();

    /**
     * 删除聊天记录
     */
    public void deleteChatRecord();
}
