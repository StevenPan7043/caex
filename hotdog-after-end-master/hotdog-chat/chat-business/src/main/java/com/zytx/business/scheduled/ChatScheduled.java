package com.zytx.business.scheduled;

import com.zytx.business.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ChatScheduled {
    @Autowired
    private ChatService chatService;

    @Scheduled(cron = "0 0 1 * * ? ")
    public void chatCount() {
        chatService.onChatCount();
    }

    @Scheduled(cron = "0 0 1 * * ? ")
    public void deleteChatRecord() {
        chatService.deleteChatRecord();
    }

}
