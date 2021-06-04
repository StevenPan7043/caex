package com.zytx.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.zytx.business.controller.ChatController;
import com.zytx.business.service.ChatService;
import com.zytx.common.constant.ChatConstant;
import com.zytx.common.dto.UserDto;
import com.zytx.common.entity.CChatInfo;
import com.zytx.common.entity.CChatRecordExample;
import com.zytx.common.mapper.CChatInfoMapper;
import com.zytx.common.cache.SocketCache;
import com.zytx.common.constant.RedisKeyConstant;
import com.zytx.common.constant.DateStyleEnum;
import com.zytx.common.constant.RespEnum;
import com.zytx.common.dto.ChatMessageDto;
import com.zytx.common.entity.CChatInfoExample;
import com.zytx.common.mapper.CChatRecordMapper;
import com.zytx.common.util.*;
import io.netty.channel.Channel;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@Log4j2
public class ChatServiceImpl implements ChatService {

    @Autowired
    private CChatInfoMapper cChatInfoMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private CChatRecordMapper cChatRecordMapper;

    /**
     * 管理员禁言
     *
     * @param id      用户ID
     * @param banTime 禁言时间，精确到秒。-1表示永久封禁 0表示解封
     */
    @Override
    public String banMember(HttpServletRequest request, Integer id, Integer banTime) {
        String uid = request.getAttribute("id").toString();
        if (StringUtil.isNullOrBank(uid)) {
            return RespUtil.getFail(RespEnum.FAIL, null, "用户ID不存在");
        }
        CChatInfoExample example = new CChatInfoExample();
        example.createCriteria().andIdIn(Arrays.asList(Integer.parseInt(uid), id));
        List<CChatInfo> cChatInfos = cChatInfoMapper.selectByExample(example);
        CChatInfo managerInfo = null;
        CChatInfo userInfo = null;
        for (CChatInfo info : cChatInfos) {
            if (info.getId().equals(Integer.parseInt(uid))) managerInfo = info;
            else userInfo = info;
        }
        if (id.equals(Integer.parseInt(uid))) {
            if (managerInfo.getType() != 2) {
                return RespUtil.getFail(RespEnum.FAIL, null, "无法禁言自己");
            } else {
                userInfo = managerInfo;
            }
        }
        if (managerInfo == null || userInfo == null) {
            return RespUtil.getFail(RespEnum.FAIL, null, "用户错误");
        }
        if (managerInfo.getType() == 0 && userInfo.getType() != 1 || managerInfo.getType() == 1) {
            return RespUtil.getFail(RespEnum.FAIL, null, "权限不足，无法禁言");
        }
        CChatInfo chatInfo = new CChatInfo();
        chatInfo.setUpdateTime(new Date());
        chatInfo.setId(id);
        if (banTime == -1) {
            chatInfo.setBanTime(DateUtil.addYear(new Date(), 100));
        } else if (banTime == 0) {
            chatInfo.setBanTime(new Date());
        } else {
            if (banTime <= 0) return RespUtil.getFail(RespEnum.FAIL, null, "禁言时间错误");
            chatInfo.setBanTime(DateUtil.addSecond(new Date(), banTime));
        }
        int i = cChatInfoMapper.updateByPrimaryKeySelective(chatInfo);
        if (i < 1) {
            return RespUtil.getFail(RespEnum.FAIL, null, "禁言失败");
        }
        ChatMessageDto dto = new ChatMessageDto();
        dto.setId(id);
        //将禁言结果设置到redis中
        String result = null;
        if (banTime == 0) {
            redisUtil.deleteKey(RedisKeyConstant.CHAT_BAN + "_" + id);
            dto.setIsBan(0);
            result = RespUtil.getSuccess(RespEnum.OUT_BAN, dto);
        } else {
            redisUtil.setKey(RedisKeyConstant.CHAT_BAN + "_" + id, DateUtil.dateToString(chatInfo.getBanTime(), DateStyleEnum.YYYY_MM_DD_HH_MM_SS), DateUtil.getHMS(chatInfo.getBanTime(), new Date()) / 1000);
            dto.setIsBan(1);
            dto.setBanTime(chatInfo.getBanTime());
            result = RespUtil.getSuccess(RespEnum.ON_BAN, dto);
        }
        //给用户发送消息
        String web_token = SocketCache.getToken("WEB_" + id);
        String app_token = SocketCache.getToken("APP_" + id);
        if (!StringUtil.isNullOrBank(web_token)) {
            SendUtil.send(SocketCache.getChannel(web_token), result);
        }
        if (!StringUtil.isNullOrBank(app_token)) {
            SendUtil.send(SocketCache.getChannel(app_token), result);
        }
        return RespUtil.getSuccess(RespEnum.SUCCESS, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateManager(HttpServletRequest request, Integer id, Integer type) {
        String uid = request.getAttribute("id").toString();
        if (StringUtil.isNullOrBank(uid)) {
            return RespUtil.getFail(RespEnum.FAIL, null, "用户ID不存在");
        }
        CChatInfoExample example = new CChatInfoExample();
        example.createCriteria().andIdIn(Arrays.asList(Integer.parseInt(uid), id));
        List<CChatInfo> cChatInfos = cChatInfoMapper.selectByExample(example);
        CChatInfo managerInfo = null;
        CChatInfo userInfo = null;
        for (CChatInfo info : cChatInfos) {
            if (info.getId().equals(Integer.parseInt(uid))) managerInfo = info;
            else userInfo = info;
        }
        if (id.equals(Integer.parseInt(uid))) {
            return RespUtil.getFail(RespEnum.FAIL, null, "无法操作自己");
        }
        if (managerInfo == null || userInfo == null) {
            return RespUtil.getFail(RespEnum.FAIL, null, "用户错误");
        }
        //如果有多个群主时，群主之间不能修改对方的信息
        if (managerInfo.getType() != 2 || userInfo.getType() == 2) {
            return RespUtil.getFail(RespEnum.FAIL, null, "权限不足");
        }
        CChatInfo chatInfo = new CChatInfo();
        chatInfo.setId(id);
        if (type == 0) {
            chatInfo.setType(0);
        } else {
            chatInfo.setType(1);
        }
        int i = cChatInfoMapper.updateByPrimaryKeySelective(chatInfo);
        if (i > 0) {
            //给用户发送消息
            ChatMessageDto dto = new ChatMessageDto();
            dto.setSendId(id);
            dto.setSendType(chatInfo.getType());
            dto.setType(ChatConstant.UPDATE_MANAGER);
            String result = RespUtil.getSuccess(RespEnum.SUCCESS, dto);
            String web_token = SocketCache.getToken("WEB_" + id);
            String app_token = SocketCache.getToken("APP_" + id);
            if (!StringUtil.isNullOrBank(web_token)) {
                UserDto user = SocketCache.getUser(web_token);
                user.setType(chatInfo.getType());
                SocketCache.saveCache(web_token, user);
                SendUtil.send(SocketCache.getChannel(web_token), result);
            }
            if (!StringUtil.isNullOrBank(app_token)) {
                UserDto user = SocketCache.getUser(app_token);
                user.setType(chatInfo.getType());
                SocketCache.saveCache(app_token, user);
                SendUtil.send(SocketCache.getChannel(app_token), result);
            }
            return RespUtil.getSuccess(RespEnum.SUCCESS, null);
        } else {
            return RespUtil.getFail(RespEnum.FAIL, null, "修改失败");
        }
    }

    /**
     * 修改用户信息
     *
     * @param chatInfo
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateInfoById(HttpServletRequest request, CChatInfo chatInfo) {
        String uid = request.getAttribute("id").toString();
        if (StringUtil.isNullOrBank(uid)) {
            return RespUtil.getFail(RespEnum.FAIL, null, "用户ID不存在");
        }
        if (chatInfo.getId() == null) {
            return RespUtil.getFail(RespEnum.FAIL, null, "用户ID不存在");
        }
        CChatInfo cChatInfo = cChatInfoMapper.selectByPrimaryKey(Integer.parseInt(uid));
        if (cChatInfo == null || cChatInfo.getType() != 2) {
            return RespUtil.getFail(RespEnum.FAIL, null, "权限不足");
        }
        int i = cChatInfoMapper.updateByPrimaryKeySelective(chatInfo);
        if (i > 0) {
            return RespUtil.getSuccess(RespEnum.SUCCESS, null);
        } else {
            return RespUtil.getFail(RespEnum.FAIL, null, "修改失败");
        }
    }

    /**
     * 将禁言用户加载到redis中
     */
    @Override
    public void initChatInfo() {
        CChatInfoExample example = new CChatInfoExample();
        example.createCriteria().andBanTimeGreaterThan(new Date());
        List<CChatInfo> cChatInfos = cChatInfoMapper.selectByExample(example);
        for (CChatInfo info : cChatInfos) {
            redisUtil.setKey(RedisKeyConstant.CHAT_BAN + "_" + info.getId(), DateUtil.dateToString(info.getBanTime(), DateStyleEnum.YYYY_MM_DD_HH_MM_SS), DateUtil.getHMS(info.getBanTime(), new Date()) / 1000);
        }
    }

    /**
     * 处理聊天数
     */
    @Override
    public void onChatCount() {
        Map map = redisUtil.getHashKey(RedisKeyConstant.CHAT_COUNT);
        if (map.isEmpty()) return;
        List<Integer> list = new ArrayList<>();
        for (Object key : map.keySet()) {
            list.add(Integer.parseInt(key.toString()));
        }
        CChatInfoExample example = new CChatInfoExample();
        example.createCriteria().andIdIn(list);
        List<CChatInfo> cChatInfos = cChatInfoMapper.selectByExample(example);
        for (CChatInfo info : cChatInfos) {
            if (info.getLevel() < 5) {
                Object o = map.get(info.getId().toString());
                if (o == null || Integer.parseInt(o.toString()) <= 0) continue;
                Integer count = info.getMessageCount() + Integer.parseInt(o.toString());
                int pow = (int) Math.pow(10, info.getLevel() + 2);
                while (count > pow) {
                    info.setLevel(info.getLevel() + 1);
                    count -= pow;
                    pow = (int) Math.pow(10, info.getLevel() + 2);
                }
                info.setMessageCount(count);
            }
            cChatInfoMapper.updateByPrimaryKeySelective(info);
            redisUtil.deleteHashKey(RedisKeyConstant.CHAT_COUNT, map.keySet().toArray());
        }
    }

    @Override
    public void deleteChatRecord() {
        /**
         * 聊天记录存存30天，超过30天删除
         */
        //获取七天前的时间
        Date lastTime = DateUtil.getLastTime(-30 * 24 * 60 * 60L);
        CChatRecordExample example = new CChatRecordExample();
        example.createCriteria().andCreateTimeLessThanOrEqualTo(lastTime);
//        PageHelper.startPage(1,10);
        cChatRecordMapper.deleteByExample(example);
    }
}
