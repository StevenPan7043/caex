package com.zytx.websocket.filter;

import io.netty.handler.ipfilter.IpFilterRule;
import io.netty.handler.ipfilter.IpFilterRuleType;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * ip过滤器
 */
public class IpFilterRuleHandler implements IpFilterRule {
    @Override
    public boolean matches(InetSocketAddress inetSocketAddress) {
        InetAddress address = inetSocketAddress.getAddress();
        return false;
    }

    /**
     * 判断是否过滤，当matches返回true时表示过滤
     *
     * @return
     */
    @Override
    public IpFilterRuleType ruleType() {
        return IpFilterRuleType.REJECT;
    }
}
