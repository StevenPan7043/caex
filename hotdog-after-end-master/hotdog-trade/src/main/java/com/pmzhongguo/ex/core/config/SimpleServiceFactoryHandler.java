package com.pmzhongguo.ex.core.config;

import com.pmzhongguo.ex.business.service.*;
import com.pmzhongguo.ex.core.web.JedisChannelEnum;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.ex.framework.service.FrmUserService;
import com.pmzhongguo.zzextool.consts.JedisChannelConst;
import com.pmzhongguo.zzextool.exception.BusinessException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;

/**
 * 初始化、同步基础信息
 * @author jary
 * @creatTime 2019/11/20 10:16 AM
 */
@Component
public class SimpleServiceFactoryHandler extends SimpleServiceFactory {
    /**
     * 同步基类list
     */
    private List<IDataProcess> simpleServicesList = new ArrayList<>();

    private CurrencyService currencyService;
    private CurrencyPairService currencyPairService;
    private FrmUserService frmUserService;
    private CmsService cmsService;
    private AppUpdateService appUpdateService;
    private RuleServiceManage ruleServiceManage;
    private MemberService memberService;

    public SimpleServiceFactoryHandler(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * 添加service
     * @param simpleService
     */
    public void addSimpService(IDataProcess simpleService){
        simpleServicesList.add(simpleService);
    }

    /**
     * 批量执行同步
     */
    private void monitorExecute() {
        simpleServicesList.forEach(service -> {
            service.dataSync(servletContext);
        });
        simpleServicesList.clear();
    }

    /**
     * 单个同步
     * @param jediChannel
     */
    public void monitorExecute(String jediChannel) {
        IDataProcess simpleService = getSimpleService(jediChannel);
        if (simpleService == null) {
            throw new BusinessException(Resp.FAIL, "单个同步获取JavaBean失败，jediChannel：" + jediChannel + "-" + JedisChannelEnum.getEnumByCode(jediChannel).getCodeCn() + ".");
        }
        simpleService.dataSync(servletContext);
    }

    @Override
    public IDataProcess getSimpleService(String jediChannel) {
        IDataProcess simpleService = null;
        switch (jediChannel) {
            case JedisChannelConst.JEDIS_CHANNEL_SYNC_CONFIG:
                frmUserService = (FrmUserService) wac.getBean("frmUserService");
                simpleService = frmUserService;                                                     break;//配置同步频道
            case JedisChannelConst.JEDIS_CHANNEL_SYNC_CURRENCY_PAIR:
                currencyPairService = (CurrencyPairService) wac.getBean("currencyPairService");
                simpleService = currencyPairService;                                                break;//交易对同步频道
            case JedisChannelConst.JEDIS_CHANNEL_CURRENCY:
                currencyService = (CurrencyService) wac.getBean("currencyService");
                simpleService = currencyService;                                                    break;//币种同步频道
            case JedisChannelConst.JEDIS_CHANNEL_SYNC_NEWS:
                cmsService = (CmsService) wac.getBean("cmsService");
                simpleService = cmsService;                                                         break;//新闻同步频道
            case JedisChannelConst.JEDIS_CHANNEL_SYNC_APP:
                appUpdateService = (AppUpdateService) wac.getBean("appUpdateService");
                simpleService = appUpdateService;                                                   break;//APP同步频道
            case JedisChannelConst.JEDIS_CHANNEL_SYNC_CURRENCY_RULE:
                ruleServiceManage = (RuleServiceManage) wac.getBean("ruleServiceManage");
                simpleService = ruleServiceManage;                                                  break;//币种规则同步频道
            case JedisChannelConst.JEDIS_CHANNEL_COUNTRY:
                memberService = (MemberService) wac.getBean("memberService");
                simpleService = memberService;                                                      break;//国际语言同步频道
        }
        if (simpleService == null) {
            logger.warn("同步获取JavaBean失败，jediChannel：{}-{}.", jediChannel,JedisChannelEnum.getEnumByCode(jediChannel).getCodeCn());
        }
        return simpleService;
    }

    /**
     * 初始化基础数据
     */
    public void initData() {
        JedisChannelEnum[] channelEnums = JedisChannelEnum.values();
        for (JedisChannelEnum enumItem : channelEnums) {
            IDataProcess simpleService = getSimpleService(enumItem.getCodeEn());
            if (simpleService == null) {
                logger.warn("初始化获取JavaBean失败，jediChannel：{}-{}「", enumItem.getCodeEn(), enumItem.getCodeEn());
                continue;
            }
            simpleServicesList.add(simpleService);
        }
        monitorExecute();
    }
}
