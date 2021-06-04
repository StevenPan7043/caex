/**
 * zzex.com Inc.
 * Copyright (c) 2019/4/25 All Rights Reserved.
 */
package com.pmzhongguo.zzexrpcprovider.config;

import com.pmzhongguo.ex.business.entity.Currency;
import com.pmzhongguo.ex.business.entity.CurrencyPair;
import com.pmzhongguo.ex.core.web.Constants;
import com.pmzhongguo.ex.framework.entity.FrmConfig;
import com.pmzhongguo.zzexrpcprovider.bean.GenericServiceImpl;
import com.pmzhongguo.zzexrpcprovider.consts.QuotationKeyConst;
import com.pmzhongguo.zzextool.consts.StaticConst;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author ：yukai
 * @date ：Created in 2019/4/25 19:39
 * @description：系统配置项监听, 监听系统配置是否发生改变
 * @version: $
 */
@Slf4j(topic = "frm_zookeeper")
@Component(value = "frmZkWatch")
@Scope(value = "prototype") //多例
public class FrmZkWatch extends GenericServiceImpl implements Watcher {

    private ZooKeeper zooKeeper;

    private String path;

    private CountDownLatch connectedFrm = new CountDownLatch(1);

    public void init(String path) throws IOException, KeeperException, InterruptedException {
        this.path = path;
        this.zooKeeper = new ZooKeeper(StaticConst.ZKAddress, StaticConst.SESSION_TIMEOUT, this);
        zooKeeper.getData(path, this, null);
        connectedFrm.countDown();
    }

    @Override
    public void process(WatchedEvent event) {
        if (event == null) {
            return;
        }
        Event.EventType eventType = event.getType();
        Event.KeeperState state = event.getState();
        String watchPath = event.getPath();

        if (Event.EventType.NodeDataChanged == eventType) {
            try {
                if (ZooKeeper.States.CONNECTING == zooKeeper.getState()) {
                    connectedFrm.await();
                }
                byte[] data = zooKeeper.getData(path, this, null);
                log.warn("<================== ZK数据同步： eventType:" + eventType + " \tstate:" + state + " \twatchPath:" + watchPath + "\t data: " + new String(data));
                // 系统配置
                if (Constants.ZK_WATCH_PATH_SYS_CONFIG.equals(watchPath)) {
                    FrmConfig mgrConfig = frmUserService.findConfig();
                    QuotationKeyConst.frmConfig = mgrConfig;
                } else if (Constants.ZK_WATCH_PATH_CURRENCY_PAIR.equals(watchPath)) {
                    // 交易对
                    QuotationKeyConst.currencyPairs = currencyService.getCurrencyPairLst();
                    log.warn("currencyPairs大小 + " + QuotationKeyConst.currencyPairs.size());
                    QuotationKeyConst.PAIRS.clear();
                    for (CurrencyPair currencyPair : QuotationKeyConst.currencyPairs) {
                        QuotationKeyConst.PAIRS.add(currencyPair.getKey_name().toLowerCase());
                    }
                    log.warn("Pairs大小 + " + QuotationKeyConst.PAIRS.size());
                } else if (Constants.ZK_WATCH_PATH_CURRENCY.equals(watchPath)) {
                    // 交易币种
                    QuotationKeyConst.currencysMap = (Map<String, Currency>) currencyService.getCurrencyMap().get("retMap");
                }
                log.warn("<========================= ZK数据同步成功");
                // 重启调度服务
                // taskManager.restartAllTask();
            } catch (InterruptedException e) {
                log.error("<--------- ZK同步成功失败, Cause by : " + e.getCause() + " ------->");
            } catch (KeeperException e) {
                log.error("<--------- ZK同步成功失败, Cause by : " + e.getCause() + " ------->");
            }
        }
    }
}
