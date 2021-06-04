package com.zytx.common.constant;

/**
 * 聊天指令
 */
public class ChatConstant {

    public static final byte LOGOUT = 0x00;//退出
    public static final byte LOGIN = 0x01;//进入
    public static final byte TEXT = 0x02;//聊天文本内容
    public static final byte HEART_BEAT = 0x03;//心跳
    public static final byte WITHDRAW = 0x04;//撤回消息
    public static final byte UPDATE_MANAGER = 0x05;//管理管理员
}
