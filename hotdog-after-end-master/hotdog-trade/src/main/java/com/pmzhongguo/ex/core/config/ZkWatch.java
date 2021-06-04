package com.pmzhongguo.ex.core.config;

import com.pmzhongguo.ex.business.service.*;

import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.web.Constants;

import com.pmzhongguo.ex.framework.entity.FrmConfig;
import com.pmzhongguo.ex.framework.service.FrmUserService;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;


import javax.servlet.ServletContext;

/**
 * @description: zk 监听
 * @date: 2019-04-02 16:01
 * @author: 十一
 */

public class ZkWatch implements Watcher{

    private static Logger logger = LoggerFactory.getLogger("zookeeper");



    private ZooKeeper zk = null;

    private String path;

    public ZkWatch(String path) {
        super();
        this.path = path;
    }

    @Override
    public void process(WatchedEvent event) {

        Event.EventType eventType = event.getType();
        Event.KeeperState state = event.getState();
        String watchPath = event.getPath();


        if(Event.EventType.NodeDataChanged == eventType) {
            try {
                byte[] data = zk.getData(path, this, null);
                logger.error("<================== ZK同步： eventType:" + eventType + " \tstate:" + state + " \twatchPath:" + watchPath
                        + "\t data: " + new String(data));
                WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();


                ServletContext servletContext = ContextLoader.getCurrentWebApplicationContext().getServletContext();
                // 系统配置
                if(Constants.ZK_WATCH_PATH_SYS_CONFIG.equals(watchPath)) {
                    FrmUserService userService = (FrmUserService) wac
                            .getBean("frmUserService");
                    FrmConfig mgrConfig = userService.findConfig();
                    servletContext.setAttribute(HelpUtils.MGRCONFIG, mgrConfig);
                    userService.cacheEoslist(servletContext);
                    userService.loadApiAccessLimitCache();
                } else if(Constants.ZK_WATCH_PATH_CURRENCY_PAIR.equals(watchPath)) {
                    // 交易对
                    CurrencyService currencyService = (CurrencyService) wac.getBean("currencyService");
                    currencyService.cacheCurrencyPair(servletContext);
                } else if(Constants.ZK_WATCH_PATH_CURRENCY.equals(watchPath)) {
                    // 交易币种
                    CurrencyService currencyService = (CurrencyService) wac.getBean("currencyService");
                    currencyService.cacheCurrency(servletContext);
                } else if(Constants.ZK_WATCH_PATH_NEWS.equals(watchPath)) {
                    // 公告文章
                    CmsService cmsService = (CmsService) wac
                            .getBean("cmsService");
                    cmsService.cacheNoticeLst(servletContext);
                    cmsService.cacheNewsLst(servletContext);
                    cmsService.cacheHelpLst(servletContext);
                } else if (Constants.ZK_WATCH_PATH_APP.equals(watchPath)) {
                    // app更新
                    AppUpdateService appUpdateService = (AppUpdateService) wac
                            .getBean("appUpdateService");
                    appUpdateService.cacheAppUpdateInfo(servletContext);
                    appUpdateService.cacheQrCode(servletContext);
                } else if (Constants.ZK_WATCH_PATH_CURRENCY_RULE.equals(watchPath)) {
                    // 币种规则缓存
                    RuleServiceManage ruleServiceManage = (RuleServiceManage) wac
                            .getBean("ruleServiceManage");
                    ruleServiceManage.cacheRuleWithCurrency(servletContext);
                }
                logger.error("<========================= ZK同步成功");
            } catch (InterruptedException e) {

                logger.error("<--------- ZK同步成功失败, Cause by : " + e.getCause()+ " ------->");
            } catch (KeeperException e) {
                logger.error("<--------- ZK同步成功失败, Cause by : " + e.getCause()+ " ------->");

            }
        }
    }

    public void start(ZooKeeper zk) throws KeeperException, InterruptedException {
        this.zk = zk;
        zk.getData(path, this, null);
    }

}
