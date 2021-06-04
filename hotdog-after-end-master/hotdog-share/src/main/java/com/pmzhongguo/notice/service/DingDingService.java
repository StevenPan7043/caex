package com.pmzhongguo.notice.service;


import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.notice.support.DingDingConstant;
import com.pmzhongguo.notice.support.DingDingMsgType;
import com.pmzhongguo.zzextool.threadpool.ZzexThreadFactory;
import com.taobao.api.ApiException;
import com.taobao.api.BaseTaobaoRequest;
import com.taobao.api.TaobaoResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * @description: 发送
 * @date: 2019-12-28 16:17
 * @author: 十一
 */
@Slf4j
public class DingDingService {


    private static ExecutorService NOTICE_THREAD_POOL = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(100)
            , new ZzexThreadFactory("dingding-pool-%d"), new ThreadPoolExecutor.AbortPolicy());


    public static Resp send(String text) {
        final String url = DingDingConstant.DING_DING_URL + DingDingConstant.ACCESS_TOKEN;
        DingTalkClient client
                = new DefaultDingTalkClient(url);
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype(DingDingMsgType.TEXT);
        OapiRobotSendRequest.Text textDto = new OapiRobotSendRequest.Text();
        textDto.setContent(text);
        request.setText(textDto);

        DingDingMessageThread dingMessageThread = new DingDingMessageThread(client, request);
        NOTICE_THREAD_POOL.execute(dingMessageThread);
        return new Resp( Resp.SUCCESS, Resp.SUCCESS_MSG);
    }

    private static class DingDingMessageThread implements Runnable {

        private DingTalkClient client;

        private BaseTaobaoRequest request;

        public DingDingMessageThread(DingTalkClient client, BaseTaobaoRequest request) {
            this.client = client;
            this.request = request;
        }

        @Override
        public void run() {
            TaobaoResponse response = null;
            try {
                response = client.execute(request);
                log.info("result: {}", JSONObject.toJSONString(response));
            } catch (ApiException e) {
                log.error("钉钉发送异常：{}",e);
            }

        }

    }














}
