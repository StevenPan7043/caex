package com.zytx.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 消息内容
 */
@Data
public class ChatMessageDto implements Serializable {
    private static final long serialVersionUID = -3818197381322360706L;

    private Integer sendId;//发送者ID
    private byte type;//消息类型，参考ChatInstruction
    private String message;//消息内容
    private Integer toId;//接受者ID
    private Integer toType;//接受者类别，1群 2用户
    private Integer isBan;//是否禁言 0否 1是
    private Date banTime;//禁言时间
    private String token;//
    private Integer id;//消息ID
    private String sendPhone;//发送者用户账号
    private String sendName;//发送者姓名
    private List<Object> list;
    private Date createTime;//消息发送时间
    private List<ChatMessageDto> messageList;//消息记录
    private Integer level;//用户等级
    private String client;//登录设备 APP、WEB
    private Integer sendType;//用户类型 0管理员 1会员 2群主
}
