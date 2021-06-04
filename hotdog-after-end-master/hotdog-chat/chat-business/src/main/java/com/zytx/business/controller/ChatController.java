package com.zytx.business.controller;

import com.zytx.business.service.ChatService;
import com.zytx.common.entity.CChatInfo;
import com.zytx.common.constant.RespEnum;
import com.zytx.common.util.RespUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ChatController {

    @Autowired
    private ChatService chatService;

    /**
     * 管理员禁言
     *
     * @param id      要禁言的用户ID
     * @param banTime 言时间，精确到秒。-1表示永久封禁 0表示解封
     * @return
     */
    @RequestMapping("banMember")
    public String banMember(HttpServletRequest request, @RequestParam Integer id, @RequestParam Integer banTime) {
        try {
            return chatService.banMember(request, id, banTime);
        } catch (Exception e) {
            e.printStackTrace();
            return RespUtil.getFail(RespEnum.FAIL, null);
        }
    }

    /**
     * 管理管理员
     *
     * @param id   用户ID
     * @param type 操作 0管理员 1会员
     * @return
     */
    @RequestMapping("updateManager")
    public String updateManager(HttpServletRequest request, @RequestParam Integer id, @RequestParam Integer type) {
        try {
            return chatService.updateManager(request, id, type);
        } catch (Exception e) {
            e.printStackTrace();
            return RespUtil.getFail(RespEnum.FAIL, null);
        }
    }

    /**
     * 修改用户信息
     *
     * @param chatInfo
     * @return
     */
    @RequestMapping("updateInfoById")
    public String updateInfoById(HttpServletRequest request, CChatInfo chatInfo) {
        try {
            return chatService.updateInfoById(request, chatInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return RespUtil.getFail(RespEnum.FAIL, null);
        }
    }
}
