/**
 * zzex.com Inc.
 * Copyright (c) 2019/7/4 All Rights Reserved.
 */
package com.pmzhongguo.zzexrpcprovider.model;

import lombok.Data;

/**
 * @author ：yukai
 * @date ：Created in 2019/7/4 11:54
 * @description：socket 接受的消息解析对象
 * @version: $
 */
@Data
public class APISocketMessage {

    private String channel;

    private Long timestamp;
}
