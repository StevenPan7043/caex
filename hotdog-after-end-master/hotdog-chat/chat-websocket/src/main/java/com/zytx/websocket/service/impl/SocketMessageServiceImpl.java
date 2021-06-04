package com.zytx.websocket.service.impl;

import com.zytx.common.constant.RedisKeyConstant;
import com.zytx.common.dto.UserDto;
import com.zytx.common.entity.*;
import com.zytx.common.mapper.CChatInfoMapper;
import com.zytx.common.mapper.CChatRecordMapper;
import com.zytx.common.cache.SocketCache;
import com.zytx.common.constant.RespEnum;
import com.zytx.common.dto.ChatMessageDto;
import com.zytx.common.mapper.MMemberMapper;
import com.zytx.common.util.*;
import com.zytx.common.constant.ChatConstant;
import com.zytx.websocket.service.SocketMessageService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Log4j2
public class SocketMessageServiceImpl implements SocketMessageService {
    @Autowired
    private CChatInfoMapper cChatInfoMapper;
    @Autowired
    private CChatRecordMapper cChatRecordMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private MMemberMapper mMemberMapper;

    @Override
    public void login(ChannelHandlerContext ctx, ChatMessageDto message) {
        if (StringUtil.isNullOrBank(message.getClient())) {
            log.warn("登录设备错误");
            String resp = RespUtil.getFail(RespEnum.FAIL, null);
            SendUtil.send(ctx.channel(), resp);
            ctx.channel().close();
            return;
        }
        CChatInfo chatInfo = cChatInfoMapper.selectByPrimaryKey(message.getSendId());
        MMember mMember = mMemberMapper.selectByPrimaryKey(message.getSendId());
        if (mMember == null) {
            log.warn("用户不存在");
            String resp = RespUtil.getFail(RespEnum.NO_USER, null);
            SendUtil.send(ctx.channel(), resp);
            ctx.channel().close();
            return;
        }
        //删除缓存，此操作是当账号被挤掉时清除缓存
        SocketCache.delCache(message.getClient().toUpperCase() + "_" + message.getSendId());
        //生成token
        String token = JWTUtil.getSocketToken(mMember.getId(), message.getClient().toUpperCase(), mMember.getmName());

        if (chatInfo == null) {
            chatInfo = new CChatInfo();
            chatInfo.setId(message.getSendId());
            chatInfo.setCreateTime(new Date());
            chatInfo.setBanTime(chatInfo.getCreateTime());
            chatInfo.setType(1);
            chatInfo.setUpdateTime(chatInfo.getCreateTime());
            cChatInfoMapper.insertSelective(chatInfo);
        }
        ChatMessageDto result = new ChatMessageDto();
        result.setId(chatInfo.getId());
        result.setIsBan(DateUtil.getHMS(chatInfo.getBanTime(), new Date()) > 0 ? 1 : 0);
        if (result.getIsBan() == 1) {
            result.setBanTime(chatInfo.getBanTime());
        }
        result.setType(ChatConstant.LOGIN);
        result.setSendPhone(mMember.getmName());
        result.setSendName(mMember.getmNickName() == null ? "" : mMember.getmNickName());
        result.setLevel(chatInfo.getLevel());
        result.setSendType(chatInfo.getType());

        //加入缓存
        SocketCache.saveCache(ctx.channel(), token);
        UserDto userDto = new UserDto();
        userDto.setId(mMember.getId());
        userDto.setPhone(mMember.getmName());
        userDto.setName(mMember.getmNickName() == null ? "" : mMember.getmNickName());
        userDto.setLevel(chatInfo.getLevel());
        userDto.setType(chatInfo.getType());
        SocketCache.saveCache(token, userDto);

        Set<String> setKey = redisUtil.getSetKey(RedisKeyConstant.GROUP + "_1");
        setKey.forEach(k -> {
            Channel channel = SocketCache.getChannel(k);
            if (channel != null) {
                SendUtil.send(channel, RespUtil.getSuccess(RespEnum.SUCCESS, result));
            }
        });

        redisUtil.joinGroup(RedisKeyConstant.GROUP + "_1", token);
        //在线用户信息
        result.setList(queryUserInfo());
        //历史聊天记录
        result.setMessageList(queryChatRecord());
        //生成token
        result.setToken(token);
        SendUtil.send(ctx.channel(), RespUtil.getSuccess(RespEnum.SUCCESS, result));
    }

    @Override
    public void doMessage(ChannelHandlerContext ctx, ChatMessageDto message) {
        if (StringUtil.isNullOrBank(message.getToken())) {
            log.warn("token错误");
            SendUtil.send(ctx.channel(), RespUtil.getFail(RespEnum.FAIL, null));
        }
        if (!redisUtil.isInGroup(RedisKeyConstant.GROUP + "_1", message.getToken())) {
            log.warn("不在聊天室中");
            SendUtil.send(ctx.channel(), RespUtil.getFail(RespEnum.NO_IN_GROUP, null));
            return;
        }
        String key = redisUtil.getKey(RedisKeyConstant.CHAT_BAN + "_" + message.getSendId());
//        CChatInfo chatInfo = cChatInfoMapper.selectByPrimaryKey(message.getSendId());
        if (!StringUtil.isNullOrBank(key)) {
            log.warn("被禁言");
            SendUtil.send(ctx.channel(), RespUtil.getFail(RespEnum.ON_BAN, null));
            return;
        }
        CChatRecord record = new CChatRecord();
        record.setMemberId(message.getSendId());
        record.setType(1);
        /**
         * 注：因为目前只有一个聊天室，所以聊天室默认为1，
         */
        record.setToId(message.getToId() == null ? 1 : message.getToId());
        record.setToType(message.getToType() == null ? 1 : message.getToId());
        record.setMessage(message.getMessage());
        record.setCreateTime(new Date());

        redisUtil.incrementHash(RedisKeyConstant.CHAT_COUNT, message.getSendId().toString(), 1L);
        record.setIsSuccess(1);
        //获取发送者信息
        UserDto user = SocketCache.getUser(message.getToken());
        message.setSendName(user.getName() == null ? "" : user.getName());
        message.setSendPhone(user.getPhone());
        message.setCreateTime(record.getCreateTime());
        message.setSendType(user.getType());
        cChatRecordMapper.insertSelective(record);
        message.setId(record.getId());
        if (record.getToType() == 1) {
            boolean flag = false;
            //获取聊天室中的用户
            Set<String> setKey = redisUtil.getSetKey(RedisKeyConstant.GROUP + "_" + record.getToId());
            for (String k : setKey) {
                Channel channel = SocketCache.getChannel(k);
                if (channel != null) {
                    Boolean send = SendUtil.send(channel, RespUtil.getSuccess(RespEnum.SUCCESS, message));
                    //判断是否发送成功，只有要1个即为成功
                    if (send) {
                        flag = true;
                    }
                }
            }
            if (!flag) {
                //发送失败时更改状态
                record.setIsSuccess(0);
                cChatRecordMapper.updateByPrimaryKey(record);
            }
        } else {
            //暂无其他
        }
    }

    @Override
    public void logout(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        if (channel == null) return;
        String token = SocketCache.getToken(channel);
        /**
         * 因为只有一个聊天室，所以写成定值
         * 如果后面加需求有多个聊天室时需要获取用户所有的聊天室
         */
        if (!StringUtil.isNullOrBank(token)) {
            redisUtil.exitGroup(RedisKeyConstant.GROUP + "_1", token);
            UserDto user = SocketCache.getUser(token);
            if (user == null) return;
            SocketCache.delCache(channel);
            ChatMessageDto dto = new ChatMessageDto();
            dto.setSendId(user.getId());
            dto.setSendName(user.getName() == null ? "" : user.getName());
            dto.setSendPhone(user.getPhone());
            dto.setType(ChatConstant.LOGOUT);
            //获取聊天室中的用户
            Set<String> setKey = redisUtil.getSetKey(RedisKeyConstant.GROUP + "_1");
            setKey.forEach(k -> {
                Channel channel1 = SocketCache.getChannel(k);
                if (channel1 != null) {
                    SendUtil.send(channel1, RespUtil.getSuccess(RespEnum.SUCCESS, dto));
                }
            });
        }
    }

    @Override
    public void withdraw(ChannelHandlerContext ctx, ChatMessageDto message) {
        CChatRecord cChatRecord = cChatRecordMapper.selectByPrimaryKey(message.getId());
        CChatInfo cChatInfo = cChatInfoMapper.selectByPrimaryKey(message.getSendId());
        if (cChatInfo.getType() == 0 || cChatInfo.getType() == 2 || cChatRecord.getMemberId().equals(message.getSendId()) && DateUtil.getHMS(new Date(), cChatRecord.getCreateTime()) < 5 * 60 * 1000L) {
            //获取聊天室中的用户
            Set<String> setKey = redisUtil.getSetKey(RedisKeyConstant.GROUP + "_1");
            for (String k : setKey) {
                Channel channel = SocketCache.getChannel(k);
                if (channel != null) {
                    SendUtil.send(channel, RespUtil.getSuccess(RespEnum.SUCCESS, message));
                }
            }
            cChatRecordMapper.deleteByPrimaryKey(message.getId());
        } else {
            SendUtil.send(ctx.channel(), RespUtil.getFail(RespEnum.FAIL, null, "撤回失败"));
        }
    }

    /**
     * 获取在线用户的信息
     *
     * @return
     */
    private List<Object> queryUserInfo() {
        Set<String> setKey = redisUtil.getSetKey(RedisKeyConstant.GROUP + "_1");
        if (setKey.isEmpty()) return new ArrayList<>();
        List<Object> result = new ArrayList<>();
        for (String s : setKey) {
            UserDto user = SocketCache.getUser(s);
            result.add(user);
        }
        return result;
    }

    /**
     * 获取聊天信息
     *
     * @return
     */
    private List<ChatMessageDto> queryChatRecord() {
        Map<String, Object> map = new HashMap<>();
        map.put("limit", 100);
        return cChatRecordMapper.queryChatRecord(map);
    }
}
