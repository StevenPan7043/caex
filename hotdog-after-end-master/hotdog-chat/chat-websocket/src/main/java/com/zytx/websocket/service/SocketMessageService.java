package com.zytx.websocket.service;

import com.zytx.common.dto.ChatMessageDto;
import io.netty.channel.ChannelHandlerContext;

public interface SocketMessageService {

    /**
     * 用户登录处理
     *
     * @param ctx
     * @param message
     * @return
     */
    public void login(ChannelHandlerContext ctx, ChatMessageDto message);

    /**
     * 消息处理
     *
     * @param ctx
     * @param message
     * @return
     */
    public void doMessage(ChannelHandlerContext ctx, ChatMessageDto message);

    /**
     * 退出登录
     *
     * @param ctx
     */
    public void logout(ChannelHandlerContext ctx);

    /**
     * 撤回消息
     *
     * @param ctx
     * @param message
     */
    public void withdraw(ChannelHandlerContext ctx, ChatMessageDto message);
}
